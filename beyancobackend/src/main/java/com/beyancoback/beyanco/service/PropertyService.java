package com.beyancoback.beyanco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.beyancoback.beyanco.model.ChatHistory;
import com.beyancoback.beyanco.model.Property;
import com.beyancoback.beyanco.model.User;
import com.beyancoback.beyanco.repo.ChatHistoryRepository;
import com.beyancoback.beyanco.repo.PropertyRepository;
import com.beyancoback.beyanco.repo.UserRepo;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@Service
public class PropertyService {

	private final PropertyRepository propertyRepository;
	private final UserRepo userRepository;
	private final AiImageService aiService;

	private final String uploadDir = "C:/myapp/uploads/";
	private final String generatedDir = "C:/myapp/generated/";
	
	@Autowired
	private ChatHistoryRepository chatHistoryRepository; // ✅ inject repository

	public PropertyService(PropertyRepository propertyRepository, UserRepo userRepository,
			AiImageService aiService) {
		this.propertyRepository = propertyRepository;
		this.userRepository = userRepository;
		this.aiService = aiService;

		new File(uploadDir).mkdirs();
		new File(generatedDir).mkdirs();
	}

//	public Property uploadProperty(Long userId, String title, String description, String propertyType,
//			String enhancementType, String enhancementStyle, MultipartFile file) {
//		try {
//// 🔹 Find user
//			User user = userRepository.findById(userId.intValue())
//				    .orElseThrow(() -> new RuntimeException("User not found"));
//
////			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//
//// 🔹 Save uploaded file
//			String originalFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//			String uploadPath = uploadDir + originalFilename;
//			file.transferTo(new File(uploadPath));
//
//// 🔹 Read & encode as base64
//			byte[] fileBytes = Files.readAllBytes(new File(uploadPath).toPath());
//			String base64 = Base64.getEncoder().encodeToString(fileBytes);
//
//// 🔹 Call AI & get generated image
////			String generatedBase64 = aiService.sendImageBase64ToAi(base64);
////			 🔹 Call AI & get generated image
//			String generatedBase64 = aiService.sendImageBase64ToAi(
//			    base64,
//			    title,
//			    description,
//			    propertyType,
//			    enhancementStyle
//			);
//
//
//// ✅ Defensive check
//			if (generatedBase64 == null || generatedBase64.isBlank()) {
//				throw new RuntimeException("AI server did not return a valid image");
//			}
//
//// 🔹 Save generated image
//			String generatedFilename = "generated_" + originalFilename;
//			String generatedPath = generatedDir + generatedFilename;
//
//// Decode base64 and write to file
//			byte[] generatedBytes = Base64.getDecoder().decode(generatedBase64);
//			try (OutputStream out = new FileOutputStream(generatedPath)) {
//				out.write(generatedBytes);
//			}
//
//// 🔹 Save in DB
//			Property property = new Property(title, description, propertyType, enhancementType, enhancementStyle,
//					"/uploads/" + originalFilename, "/generated/" + generatedFilename, user);
//			return propertyRepository.save(property);
//
//		} catch (IOException e) {
//			throw new RuntimeException("Failed to process upload", e);
//		} catch (Exception e) {
//			throw new RuntimeException("Unexpected error while uploading property", e);
//		}
//	}
	
	public Property uploadProperty(Long userId, String title, String description, String propertyType,
			String enhancementType, String enhancementStyle, MultipartFile file, Long chatHistoryId) {
		try {
// 🔹 Find user
			User user = userRepository.findById(userId.intValue())
				    .orElseThrow(() -> new RuntimeException("User not found"));

//			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

			
			// 🔹 (optional) Find chat history
			ChatHistory chatHistory = null;
			if (chatHistoryId != null) {
			    chatHistory = chatHistoryRepository.findById(chatHistoryId)
			        .orElseThrow(() -> new RuntimeException("ChatHistory not found"));
			}
// 🔹 Save uploaded file
			String originalFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			String uploadPath = uploadDir + originalFilename;
			file.transferTo(new File(uploadPath));

// 🔹 Read & encode as base64
			byte[] fileBytes = Files.readAllBytes(new File(uploadPath).toPath());
			String base64 = Base64.getEncoder().encodeToString(fileBytes);

// 🔹 Call AI & get generated image
//			String generatedBase64 = aiService.sendImageBase64ToAi(base64);
//			 🔹 Call AI & get generated image
			String generatedBase64 = aiService.sendImageBase64ToAi(
			    base64,
			    title,
			    description,
			    propertyType,
			    enhancementStyle
			);


// ✅ Defensive check
			if (generatedBase64 == null || generatedBase64.isBlank()) {
				throw new RuntimeException("AI server did not return a valid image");
			}

// 🔹 Save generated image
			String generatedFilename = "generated_" + originalFilename;
			String generatedPath = generatedDir + generatedFilename;

// Decode base64 and write to file
			byte[] generatedBytes = Base64.getDecoder().decode(generatedBase64);
			try (OutputStream out = new FileOutputStream(generatedPath)) {
				out.write(generatedBytes);
			}

// 🔹 Save in DB
			Property property = new Property(title, description, propertyType, enhancementType, enhancementStyle,
					"/uploads/" + originalFilename, "/generated/" + generatedFilename, user);
//			Property property = new Property(title, description, propertyType, enhancementType, enhancementStyle,
//					"/uploads/" + originalFilename, "/generated/" + originalFilename, user);
			property.setChatHistory(chatHistory);
			return propertyRepository.save(property);

		} catch (IOException e) {
			throw new RuntimeException("Failed to process upload", e);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while uploading property", e);
		}
	}

	public List<Property> getPropertiesByUserId(Long userId) {
		return propertyRepository.findByUserId(userId);
	}

	public Property getPropertyByIdForUser(Long id, Long userId) {
		return propertyRepository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new RuntimeException("Property not found or unauthorized"));
	}

	public void deleteProperty(Long id, Long userId) {
		Property property = getPropertyByIdForUser(id, userId);
		propertyRepository.delete(property);
	}

	public Property enhanceProperty(Long id, Long userId) {
		Property property = getPropertyByIdForUser(id, userId);
		property.setEnhancementType("enhanced");
		property.setEnhancementStyle("new style");
		return propertyRepository.save(property);
	}
	public List<Property> getAllProperties() {
	    return propertyRepository.findAll();
	}

	public List<Property> getPropertiesByChatId(Long chatId) {
        return propertyRepository.findByChatHistoryId(chatId);
    }
}

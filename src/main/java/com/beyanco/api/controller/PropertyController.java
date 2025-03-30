package com.beyanco.api.controller;

import com.beyanco.api.model.Property;
import com.beyanco.api.model.User;
import com.beyanco.api.payload.response.MessageResponse;
import com.beyanco.api.repository.PropertyRepository;
import com.beyanco.api.repository.UserRepository;
import com.beyanco.api.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    private final String uploadDir = "uploads/";

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadPropertyImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("propertyType") String propertyType,
            @RequestParam("enhancementType") String enhancementType,
            @RequestParam("enhancementStyle") String enhancementStyle,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            // Get user from database
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if user has enough credits
            if (user.getCreditsRemaining() <= 0 && !userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Not enough credits. Please upgrade your subscription."));
            }

            // Create property
            Property property = new Property();
            property.setTitle(title);
            property.setDescription(description);
            property.setPropertyType(propertyType);
            property.setEnhancementType(enhancementType);
            property.setEnhancementStyle(enhancementStyle);
            property.setUser(user);

            // Save original image
            String originalFilename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path originalPath = Paths.get(uploadDir + originalFilename);
            Files.createDirectories(originalPath.getParent());
            Files.copy(file.getInputStream(), originalPath);
            property.setOriginalImageUrl("/api/properties/images/" + originalFilename);

            // For now, the enhanced image is the same as original (will be replaced with actual processing)
            property.setImageUrl(property.getOriginalImageUrl());

            // Save property
            propertyRepository.save(property);

            // Deduct credit (unless admin)
            if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                user.setCreditsRemaining(user.getCreditsRemaining() - 1);
                userRepository.save(user);
            }

            return ResponseEntity.ok(property);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Failed to upload image: " + e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Property>> getUserProperties(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Property> properties = propertyRepository.findByUserOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getProperty(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Property> propertyOpt = propertyRepository.findById(id);

        if (propertyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Property property = propertyOpt.get();

        // Check if the property belongs to the user or the user is an admin
        if (!property.getUser().getId().equals(userDetails.getId()) &&
                !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("You don't have permission to view this property"));
        }

        return ResponseEntity.ok(property);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Property> propertyOpt = propertyRepository.findById(id);

        if (propertyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Property property = propertyOpt.get();

        // Check if the property belongs to the user or the user is an admin
        if (!property.getUser().getId().equals(userDetails.getId()) &&
                !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("You don't have permission to delete this property"));
        }

        propertyRepository.delete(property);
        return ResponseEntity.ok(new MessageResponse("Property deleted successfully"));
    }

    // Basic simulation of AI enhancement processing
    @PostMapping("/{id}/enhance")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> enhanceProperty(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Property> propertyOpt = propertyRepository.findById(id);

        if (propertyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Property property = propertyOpt.get();

        // Check if the property belongs to the user or the user is an admin
        if (!property.getUser().getId().equals(userDetails.getId()) &&
                !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new MessageResponse("You don't have permission to enhance this property"));
        }

        // In a real app, this would trigger AI processing of the image
        // For demo, we're just updating the timestamp to simulate processing
        property.setUpdatedAt(LocalDateTime.now());
        propertyRepository.save(property);

        return ResponseEntity.ok(property);
    }
}

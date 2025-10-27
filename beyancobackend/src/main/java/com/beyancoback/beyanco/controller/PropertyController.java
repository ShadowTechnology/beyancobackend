package com.beyancoback.beyanco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.beyancoback.beyanco.model.Property;
import com.beyancoback.beyanco.payload.response.MessageResponse;
import com.beyancoback.beyanco.security.services.UserDetailsImpl;
import com.beyancoback.beyanco.service.PropertyService;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("Property API is working!");
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }


    @GetMapping
    public ResponseEntity<List<Property>> getUserProperties(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Property> properties = propertyService.getPropertiesByUserId(userDetails.getId());
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getProperty(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Property property = propertyService.getPropertyByIdForUser(id, userDetails.getId());
        return ResponseEntity.ok(property);
    }

//    @PostMapping("/upload")
//    public ResponseEntity<Property> uploadProperty(
//            @RequestParam("title") String title,
//            @RequestParam("description") String description,
//            @RequestParam("propertyType") String propertyType,
//            @RequestParam(value = "enhancementType", required = false) String enhancementType,
//            @RequestParam(value = "enhancementStyle", required = false) String enhancementStyle,
//            @RequestParam("file") MultipartFile file,
//            @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//        Property property = propertyService.uploadProperty(
//                userDetails.getId(),
//                title,
//                description,
//                propertyType,
//                enhancementType,
//                enhancementStyle,
//                file
//        );
//        return ResponseEntity.ok(property);
//    }
    
    @PostMapping("/upload")
    public ResponseEntity<Property> uploadProperty(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("propertyType") String propertyType,
            @RequestParam(value = "enhancementType", required = false) String enhancementType,
            @RequestParam(value = "enhancementStyle", required = false) String enhancementStyle,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "chatHistoryId", required = false) Long chatHistoryId, // ✅ NEW
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Property property = propertyService.uploadProperty(
                userDetails.getId(),
                title,
                description,
                propertyType,
                enhancementType,
                enhancementStyle,
                file,
                chatHistoryId // ✅ NEW
        );
        return ResponseEntity.ok(property);
    }


    @PostMapping("/{id}/enhance")
    public ResponseEntity<Property> enhanceProperty(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Property enhanced = propertyService.enhanceProperty(id, userDetails.getId());
        return ResponseEntity.ok(enhanced);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProperty(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        propertyService.deleteProperty(id, userDetails.getId());
        return ResponseEntity.ok(new MessageResponse("Property deleted successfully"));
    }
    
    @GetMapping("/byChat/{chatId}")
    public ResponseEntity<List<Property>> getPropertiesByChat(@PathVariable Long chatId) {
        List<Property> list = propertyService.getPropertiesByChatId(chatId);
        return ResponseEntity.ok(list);
    }

}

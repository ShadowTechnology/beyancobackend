package com.beyancoback.beyanco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AiController {

    @PostMapping("/generate-image")
    public ResponseEntity<?> generateImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok("AI processed image successfully (mock response)");
    }
}

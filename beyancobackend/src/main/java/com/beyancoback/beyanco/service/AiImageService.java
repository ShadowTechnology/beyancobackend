package com.beyancoback.beyanco.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.io.Console;
import java.time.Duration;
import java.util.Map;

@Service
public class AiImageService {

    private final WebClient webClient;

    public AiImageService(@Value("${ai.service.url}") String baseUrl) {
        // Increase max in-memory size for large base64 images (e.g., 20 MB)
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(20 * 1024 * 1024)) // 20 MB
            .build();

        // Increase timeout (e.g., up to 5 minutes)
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .filter((request, next) -> {
                System.out.println("➡️ Sending request: " + request.method() + " " + request.url());
                return next.exchange(request);
            })
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .responseTimeout(Duration.ofMinutes(5))
            ))
            .exchangeStrategies(strategies)
            .build();
        System.out.println("AI server baseUrl: " + baseUrl);
    }

//    public String sendImageBase64ToAi(String base64) {
//        String cleanBase64 = base64.replaceFirst("^data:image/[^;]+;base64,", "");
//
//        Map<String, Object> requestBody = Map.of(
//            "image_base64", cleanBase64,
//            "prompt", "Fill the room with furnitures and lamps and wallclock and place a teapot plus a book wardrobe and paintings on the walls",
//            "request_id", "23072025-openai-002",
//            "provider", "openai",
//            "enhance_prompt", "true"
//        );
//        
//
//
//        System.out.println("AI Base64 "+ cleanBase64);
//        try {
////            Map<String, Object> response = webClient.post()
////                .uri("/edit-image")
////                .bodyValue(requestBody)
////                .retrieve()
////                .bodyToMono(Map.class)
////                .block();
//            Map<String, Object> response = webClient.post()
//                    .uri("/edit-image")
//                    .bodyValue(requestBody)
//                    .retrieve()
//                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
//                    .block();
//            
//            System.out.println("AI Response "+response);
//
//            String generatedBase64 = (String) response.get("output_base64_str");
//
//            if (generatedBase64 == null || generatedBase64.isBlank()) {
//                throw new RuntimeException("AI server returned no generated_base64");
//            }
//
//            return generatedBase64;
//
//        } catch (WebClientResponseException e) {
//            System.err.println("AI server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
//            throw new RuntimeException("AI server returned error: " + e.getStatusCode(), e);
//        } catch (Exception e) {
//            System.err.println("Unexpected error: " + e.getMessage());
//            throw new RuntimeException("Failed to call AI server", e);
//        }
//    }
    public String sendImageBase64ToAi(
            String base64,
            String title,
            String description,
            String propertyType,
            String enhancementStyle
    ) {
        String cleanBase64 = base64.replaceFirst("^data:image/[^;]+;base64,", "");

        // Build dynamic prompt
        StringBuilder promptBuilder = new StringBuilder();
        if (title != null && !title.isBlank()) promptBuilder.append(title).append(". ");
        if (description != null && !description.isBlank()) promptBuilder.append(description).append(". ");
        if (propertyType != null && !propertyType.isBlank()) promptBuilder.append("This is a ").append(propertyType).append(". ");
        if (enhancementStyle != null && !enhancementStyle.isBlank()) promptBuilder.append("Enhance it in ").append(enhancementStyle).append(" style. ");

        String finalPrompt = promptBuilder.toString().trim();

        Map<String, Object> requestBody = Map.of(
            "image_base64", cleanBase64,
            "prompt", finalPrompt,
            "request_id", "23072025-openai-002",
            "provider", "openai",
            "enhance_prompt", "true"
        );

        try {
            Map<String, Object> response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

//            System.out.println("AI Response " + response);
            System.out.println("🧠 AI Response: " + new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));


            // ✅ Get nested "output" object
            Object outputObj = response.get("output");
            Map<String, Object> output = null;

            if (outputObj instanceof Map<?, ?>) {
                // Safe cast with suppression of unchecked warning inside local scope
                @SuppressWarnings("unchecked")
                Map<String, Object> safeOutput = (Map<String, Object>) outputObj;
                output = safeOutput;
            }

            String generatedBase64 = (output != null) ? (String) output.get("output_base64_str") : null;

            if (generatedBase64 == null || generatedBase64.isBlank()) {
                throw new RuntimeException("AI server returned no generated_base64");
            }

            return generatedBase64;

        } catch (WebClientResponseException e) {
            System.err.println("AI server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("AI server returned error: " + e.getStatusCode(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Failed to call AI server", e);
        }
    }

}

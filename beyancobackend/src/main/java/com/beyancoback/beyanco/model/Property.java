package com.beyancoback.beyanco.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String propertyType;
    private String enhancementType;
    private String enhancementStyle;

    private String originalImageUrl;    // path to uploaded image
    private String generatedImageUrl;   // path to AI-generated image

    private LocalDateTime createdAt = LocalDateTime.now();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"properties", "password", "createdBy", "updatedBy"})
//    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({
        "properties", "password", "createdBy", "updatedBy",
        "hibernateLazyInitializer", "handler" // ✅ ignore Hibernate proxy fields
    })
    private User user;

    public Property() {}

    public Property(String title, String description, String propertyType, String enhancementType,
                    String enhancementStyle, String originalImageUrl, String generatedImageUrl, User user) {
        this.title = title;
        this.description = description;
        this.propertyType = propertyType;
        this.enhancementType = enhancementType;
        this.enhancementStyle = enhancementStyle;
        this.originalImageUrl = originalImageUrl;
        this.generatedImageUrl = generatedImageUrl;
        this.user = user;
    }

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public String getEnhancementType() { return enhancementType; }
    public void setEnhancementType(String enhancementType) { this.enhancementType = enhancementType; }

    public String getEnhancementStyle() { return enhancementStyle; }
    public void setEnhancementStyle(String enhancementStyle) { this.enhancementStyle = enhancementStyle; }

    public String getOriginalImageUrl() { return originalImageUrl; }
    public void setOriginalImageUrl(String originalImageUrl) { this.originalImageUrl = originalImageUrl; }

    public String getGeneratedImageUrl() { return generatedImageUrl; }
    public void setGeneratedImageUrl(String generatedImageUrl) { this.generatedImageUrl = generatedImageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_history_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"user", "hibernateLazyInitializer", "handler"})
    private ChatHistory chatHistory;
    
    public ChatHistory getChatHistory() { return chatHistory; }
    public void setChatHistory(ChatHistory chatHistory) { this.chatHistory = chatHistory; }
}

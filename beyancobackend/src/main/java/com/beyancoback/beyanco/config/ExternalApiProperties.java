package com.beyancoback.beyanco.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "external")
public class ExternalApiProperties {
    private String aiEditUrl;

    public String getAiEditUrl() {
        return aiEditUrl;
    }

    public void setAiEditUrl(String aiEditUrl) {
        this.aiEditUrl = aiEditUrl;
    }
}

package com.beyanco.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new HashMap<>();

        try {
            String dbStatus = "UP";
            String dbVersion = "";

            try {
                dbVersion = jdbcTemplate.queryForObject("SELECT VERSION()", String.class);
            } catch (Exception e) {
                dbStatus = "DOWN";
                dbVersion = "Error: " + e.getMessage();
            }

            response.put("status", "UP");
            response.put("database", Map.of(
                    "status", dbStatus,
                    "version", dbVersion
            ));
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(503).body(response);
        }
    }
}

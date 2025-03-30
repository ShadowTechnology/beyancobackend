package com.beyanco.api.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private String companyName;
    private String subscriptionType;
    private Integer creditsRemaining;

    public JwtResponse(String token, Long id, String username, String email, List<String> roles,
                      String firstName, String lastName, String companyName,
                      String subscriptionType, Integer creditsRemaining) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.subscriptionType = subscriptionType;
        this.creditsRemaining = creditsRemaining;
    }
}

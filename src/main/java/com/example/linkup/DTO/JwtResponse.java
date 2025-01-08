package com.example.linkup.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String username;
    private String phoneNumber;
    private String pictureUrl;
    private String address;

    private String role;
}

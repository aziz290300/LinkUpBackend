package com.example.linkup.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SignupRequest {
    private String email;
    private String username;
    private String password;
    private String address;

    private String phoneNumber;
    private String pictureUrl;
    private String role;
}

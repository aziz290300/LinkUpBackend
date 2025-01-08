package com.example.linkup.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    private String username;
    @Column(nullable = false)
    private String password;
    private String address;

    private String phoneNumber;
    private String pictureUrl;

    @Column(nullable = true)
    private String passwordResetToken;

    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany ( mappedBy="utilisateur")
    private List<Academie> academies;


}

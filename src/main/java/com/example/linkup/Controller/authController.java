package com.example.linkup.Controller;

import com.example.linkup.DTO.JwtResponse;
import com.example.linkup.DTO.LoginRequest;
import com.example.linkup.DTO.SignupRequest;
import com.example.linkup.Entities.Role;
import com.example.linkup.Entities.User;
import com.example.linkup.Repository.UserRepository;
import com.example.linkup.Security.JwtUtils;
import com.example.linkup.Security.UserDetailsImpl;
import com.example.linkup.Security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Vérification si l'email existe déjà dans la base de données
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Error: Email is already taken!"));
        }

        // Création du nouvel utilisateur
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(encoder.encode(signUpRequest.getPassword())); // Mot de passe crypté
        user.setAddress(signUpRequest.getAddress());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPictureUrl(signUpRequest.getPictureUrl());

        // Gestion des rôles
        Role role;
        try {
            // Si aucun rôle n'est spécifié, on attribue un rôle par défaut
            if (signUpRequest.getRole() == null || signUpRequest.getRole().isEmpty()) {
                role = Role.ROLE_ORGANISATEUR; // Rôle par défaut
            } else {
                // Vérifie si le rôle est valide (ROLE_ORGANISATEUR, ROLE_SUPERVISEUR ou ROLE_RESPONSABLE)
                role = Role.valueOf(signUpRequest.getRole());
            }
        } catch (IllegalArgumentException e) {
            role = Role.ROLE_SUPERVISEUR; // Si le rôle est invalide, on attribue ROLE_SUPERVISEUR
        }
        user.setRole(role);

        // Enregistrement de l'utilisateur dans la base de données
        userRepository.save(user);

        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Stocker l'utilisateur dans la session
        session.setAttribute("user", userDetails);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwt);
        jwtResponse.setId(userDetails.getId());
        jwtResponse.setEmail(userDetails.getEmail());
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setPhoneNumber(userDetails.getPhoneNumber());
        jwtResponse.setPictureUrl(userDetails.getPictureUrl());
        jwtResponse.setAddress(userDetails.getAddress());
        jwtResponse.setRole(userDetails.getAuthorities().stream().findFirst().get().getAuthority());

        return ResponseEntity.ok(jwtResponse);
    }

    // Endpoint to get the current session user
    @GetMapping("/current-session")
    public ResponseEntity<?> getCurrentSession(HttpSession session) {
        // Vérifier si la session contient un utilisateur
        UserDetailsImpl userDetails = (UserDetailsImpl) session.getAttribute("user");
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "No active session found."));
        }

        return ResponseEntity.ok(userDetails);
    }

    // Endpoint to log out (invalidate the session)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // Invalider la session
        return ResponseEntity.ok(Collections.singletonMap("message", "Session terminated successfully."));
    }

}

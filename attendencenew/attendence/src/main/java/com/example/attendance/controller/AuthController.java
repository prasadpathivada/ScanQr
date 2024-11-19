package com.example.attendance.controller;

import com.example.attendance.Util.JwtUtil;
import com.example.attendance.model.Role;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        System.out.println("Received login request for email: " + user.getEmail());
        
        // Find user by email
        User foundUser = userRepository.findByEmail(user.getEmail());
        
        // If user is not found
        if (foundUser == null) {
            System.out.println("User not found for email: " + user.getEmail());
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
        
        // Log stored and entered passwords for debugging
        System.out.println("Stored password hash: " + foundUser.getPassword());
        System.out.println("Entered password: " + user.getPassword());

        // Check password
        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            System.out.println("Password matches!");
            String token = jwtUtil.generateToken(foundUser.getEmail());

            // Create response payload
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("id", foundUser.getId());  // Send the user ID
            response.put("role", foundUser.getRole()); // Add role to the response
            System.out.println("Generated token: " + token);
            return ResponseEntity.ok(response);
        } else {
            System.out.println("Passwords do not match.");
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        System.out.println("Registering user: " + user);

        // Check if the email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(409).body("Email already exists");
        }

         // Set default role if not specified
         if (user.getRole() == null) {
            user.setRole(Role.USER); // Set to USER role if no role is provided
        }
        
        // Hash password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Save the new user to the database
        userRepository.save(user);
        return ResponseEntity.ok("Registration successful");
    }
}

package com.example.attendance.repository;

import com.example.attendance.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    // Find users by role
    List<User> findByRole(String role);

    // Check if a user with a specific email exists
    boolean existsByEmail(String email);
}
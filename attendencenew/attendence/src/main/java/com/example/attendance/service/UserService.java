package com.example.attendance.service;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.attendance.model.User;
import com.example.attendance.model.Role;
import com.example.attendance.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(String email, String password, Role role) {
        @SuppressWarnings("unused")
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email, hashedPassword, role);
        userRepository.save(user);
    }

    @SuppressWarnings("null")
    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null & passwordEncoder.matches(password, user.getPassword());
    }

    public User findUserById(Long id) {
        // TODO Auto-generated method stub
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        // TODO Auto-generated method stub
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        // TODO Auto-generated method stub
        Optional<User> existingUserOpt = userRepository.findById(id);
       if(existingUserOpt.isPresent()){
        User existingUser =existingUserOpt.get();
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Re-encode password
        existingUser.setRole(updatedUser.getRole());
        return userRepository.save(existingUser);

       }
       return null;
      
    }

    public void deleteUser(Long id) {
        // TODO Auto-generated method stub
        userRepository.deleteById(id);
    }
}

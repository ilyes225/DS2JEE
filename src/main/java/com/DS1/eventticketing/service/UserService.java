package com.DS1.eventticketing.service;

import com.DS1.eventticketing.model.User;
import com.DS1.eventticketing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User newUserData) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        
        User user = userOpt.get();
        
        // If email is being changed, check for duplicates
        if (!user.getEmail().equals(newUserData.getEmail()) 
            && userRepository.existsByEmail(newUserData.getEmail())) {
            throw new RuntimeException("Email already registered: " + newUserData.getEmail());
        }
        
        user.setName(newUserData.getName());
        user.setEmail(newUserData.getEmail());
        
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        
        User user = userOpt.get();
        
        // Check if user has tickets
        if (user.getTickets() != null && !user.getTickets().isEmpty()) {
            throw new RuntimeException("Cannot delete user with active tickets");
        }
        
        userRepository.deleteById(id);
    }
}
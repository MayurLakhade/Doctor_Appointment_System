package com.user.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.entities.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import com.user.util.JwtUtil;

@RestController
@RequestMapping("/uapi/users")
public class UserController {
     @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //create user
    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    // Get all users
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Get user by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // Update user details
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    //  Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    //Signup API
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    //Login API
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        Map<String, Object> response = userService.loginUser(email, password);
        return ResponseEntity.ok(response);
    }

     //Logout API
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam Long userId, @RequestHeader("Authorization") String token) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setTokenValidity(Instant.now()); //Update token validity
            userRepository.save(user);
            return ResponseEntity.ok("User logged out successfully.");
        }
        return ResponseEntity.status(404).body("User not found.");
    }

    //Validate Token API
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token, @RequestParam Long userId) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (JwtUtil.isTokenValid(token, user.getTokenValidity())) {
                return ResponseEntity.ok("Token is valid.");
            } else {
                return ResponseEntity.status(401).body("Invalid or expired token.");
            }
        }
        return ResponseEntity.status(404).body("User not found.");
    }
    
}

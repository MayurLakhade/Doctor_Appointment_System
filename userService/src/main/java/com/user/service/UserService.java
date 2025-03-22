package com.user.service;

import java.util.List;
import java.util.Map;

import com.user.entities.User;

public interface UserService {

    public User saveUser(User user);

    public List<User> getAllUsers();

    public User getUserById(Long id);

    public User getUserByEmail(String email);

    public User updateUser(Long id, User updatedUser);

    public void deleteUser(Long id);

    public User registerUser(User user);

    public Map<String, Object> loginUser(String email, String password);
    
}

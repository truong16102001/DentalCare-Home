package com.example.swp.service;

import com.example.swp.entity.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
    boolean existsByPhoneNumber(String phoneNumber);
    List<User> findAll();
    public long getTotalUsers();
    User findByUserId(int userId);

    String createPasswordResetToken(User user);
}

package com.example.swp.service.Impl;

import com.example.swp.entity.Role;
import com.example.swp.entity.Token;
import com.example.swp.entity.User;
import com.example.swp.repository.RoleRepository;
import com.example.swp.repository.TokenRepository;
import com.example.swp.repository.UserRepository;
import com.example.swp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl  implements UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private RoleRepository roleRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

    @Override
    public User findByUserId(int userId){
        return userRepository.findByUserId(userId);
    }

    @Transactional
    public String createPasswordResetToken(User user) {
        Token resetToken = tokenRepository.findByUser(user).orElse(new Token());
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);
        return resetToken.getToken();
    }

    @Override
    public List<User> findByRoleId(Integer roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        return userRepository.findByRole(role);
    }

}

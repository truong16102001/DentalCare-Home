package com.example.swp.repository;

import com.example.swp.entity.Token;
import com.example.swp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
    void deleteByUser(User user);
    Optional<Token> findByUser(User user);
}

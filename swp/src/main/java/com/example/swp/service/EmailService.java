package com.example.swp.service;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String text);
    public void sendEmail(String email, String message);
    public String generateRandom();
}

package com.example.swp.service;

import com.example.swp.entity.Booking;
import com.example.swp.entity.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionService {
    List<Session> findAll();
    Optional<Session> findById(int sessionId);
    Session save(Session session);
    List<Session> getBySessionDate(LocalDate fromDate, LocalDate toDate);

    List<Session> findBySessionDateAndStatusNot(LocalDate date, String status);

    Session findByBooking(Booking booking);
}

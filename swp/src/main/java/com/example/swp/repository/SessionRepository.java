package com.example.swp.repository;

import com.example.swp.entity.Booking;
import com.example.swp.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session> findBySessionDateBetween(LocalDate fromDate,LocalDate toDate);

    List<Session> findBySessionDateAndStatusNot(LocalDate date, String status);

    Session findByBooking(Booking booking);
}

package com.example.swp.service;

import com.example.swp.entity.Booking;
import com.example.swp.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking save(Booking booking);
    List<Booking> searchBookings(String status, Date registered_time_from, Date registered_time_to);
    Optional<Booking> findById(Integer bookingId);
    void delete(Booking booking);
    List<Booking> searchByPatient(User u);
    List<Booking> searchByPhoneNumber(String phone);
}

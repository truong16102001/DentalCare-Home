package com.example.swp.repository;

import com.example.swp.entity.Booking;
import com.example.swp.entity.Service;

import java.util.List;

public interface DentalCareCustomRepository {
    List<Service> searchServices(String key, String value, String type, int limit, int offset);
    int countServices(String key);

    Service findById(int serviceId);
    List<Service> getRelatedServices(int serviceId);
    List<Booking> findBookingsByUserId(int userId);
    List<Booking> findBookingsByPhoneNumber(String phoneNumber);
}

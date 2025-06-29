package com.example.swp.repository;

import com.example.swp.entity.Booking;
import com.example.swp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByStatusAndRegisteredTimeBetween(String status, Date startTime, Date endTime);

    List<Booking> findByRegisteredTimeBetween(Date startTime, Date endTime);
    List<Booking> findByPatient(User u);
    List<Booking> findByPhoneNumber(String phone);
}

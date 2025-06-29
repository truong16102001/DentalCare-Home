package com.example.swp.service.Impl;

import com.example.swp.entity.Booking;
import com.example.swp.entity.User;
import com.example.swp.repository.BookingRepository;
import com.example.swp.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> searchBookings(String status, Date registered_time_from, Date registered_time_to) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(registered_time_to);
        cal.add(Calendar.DATE, 1);
        Date endOfDay = cal.getTime();
        cal.setTime(registered_time_from);
        cal.add(Calendar.DATE, -1);
        Date startOfDay = cal.getTime();
        if (status == null || status.isEmpty()) {
            return bookingRepository.findByRegisteredTimeBetween(startOfDay, endOfDay);
        }
        return bookingRepository.findByStatusAndRegisteredTimeBetween(status, startOfDay, endOfDay);
    }

    @Override
    public Optional<Booking> findById(Integer bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

    @Override
    public List<Booking> searchByPatient(User u) {
        return bookingRepository.findByPatient(u);
    }

    @Override
    public List<Booking> searchByPhoneNumber(String phone) {
        return bookingRepository.findByPhoneNumber(phone);
    }
}

package com.example.swp.service;

import com.example.swp.entity.Shift;

import java.util.List;
import java.util.Optional;

public interface ShiftService {
    List<Shift> findAll();
    Optional<Shift> findById(Integer shiftId);
}

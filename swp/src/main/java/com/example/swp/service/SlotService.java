package com.example.swp.service;

import com.example.swp.entity.Slot;

import java.util.List;
import java.util.Optional;

public interface SlotService {
    List<Slot> findAll();
    Optional<Slot> findById(Integer slotId);
}

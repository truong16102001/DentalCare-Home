package com.example.swp.service.Impl;

import com.example.swp.entity.Shift;
import com.example.swp.repository.ShiftRepository;
import com.example.swp.service.ShiftService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShiftServiceImpl implements ShiftService {
    ShiftRepository shiftRepository;
    @Override
    public List<Shift> findAll() {
        return shiftRepository.findAll();
    }

    @Override
    public Optional<Shift> findById(Integer shiftId) {
        return shiftRepository.findById(shiftId);
    }
}

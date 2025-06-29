package com.example.swp.service.Impl;

import com.example.swp.entity.Slot;
import com.example.swp.repository.SlotRepository;
import com.example.swp.service.SlotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SlotServiceImpl implements SlotService {
    private SlotRepository slotRepository;
    @Override
    public List<Slot> findAll() {
        return slotRepository.findAll();
    }

    @Override
    public Optional<Slot> findById(Integer slotId) {
        return slotRepository.findById(slotId);
    }

}

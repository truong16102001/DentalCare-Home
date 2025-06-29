package com.example.swp.service.Impl;

import com.example.swp.entity.Medicine;
import com.example.swp.repository.MedicineRepository;
import com.example.swp.service.MedicineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MedicineServiceImpl implements MedicineService {
    MedicineRepository medicineRepository;
    @Override
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    public List<Medicine> searchByName(String name) {
        return medicineRepository.findByMedicineNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<Medicine> findById(String medicineId) {
        return medicineRepository.findById(medicineId);
    }

    @Override
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
}

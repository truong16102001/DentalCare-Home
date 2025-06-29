package com.example.swp.service;

import com.example.swp.entity.Medicine;

import java.util.List;
import java.util.Optional;

public interface MedicineService {
    List<Medicine> findAll();
    List<Medicine> searchByName(String name);
    Optional<Medicine> findById(String medicineId);

    Medicine save(Medicine medicine);
}

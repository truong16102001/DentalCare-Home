package com.example.swp.repository;

import com.example.swp.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentalCareRepository extends JpaRepository<Service, Integer> {
}

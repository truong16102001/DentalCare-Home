package com.example.swp.repository;

import com.example.swp.entity.Slot;
import com.example.swp.entity.Token;
import com.example.swp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {

}

package com.example.swp.service;

import com.example.swp.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> findAll();
    Optional<Room> findById(int roomId);
}

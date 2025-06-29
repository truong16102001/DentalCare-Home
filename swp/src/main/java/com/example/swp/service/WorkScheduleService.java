package com.example.swp.service;

import com.example.swp.entity.WorkingSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkScheduleService {
    List<WorkingSchedule> getByWorkingDate(LocalDate date);
    WorkingSchedule save(WorkingSchedule w);
    Optional<WorkingSchedule> findById(Integer wcId);
}

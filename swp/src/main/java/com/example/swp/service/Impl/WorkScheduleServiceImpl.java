package com.example.swp.service.Impl;

import com.example.swp.entity.WorkingSchedule;
import com.example.swp.repository.WorkingScheduleRepository;
import com.example.swp.service.WorkScheduleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleServiceImpl implements WorkScheduleService {
    private WorkingScheduleRepository workingScheduleRepository;

    public List<WorkingSchedule> getByWorkingDate(LocalDate date){
        return workingScheduleRepository.findByDateWorking(date);
    }

    @Override
    public WorkingSchedule save(WorkingSchedule w) {
        return  workingScheduleRepository.save(w);
    }

    @Override
    public Optional<WorkingSchedule> findById(Integer wcId) {
        return workingScheduleRepository.findById(wcId);
    }
}

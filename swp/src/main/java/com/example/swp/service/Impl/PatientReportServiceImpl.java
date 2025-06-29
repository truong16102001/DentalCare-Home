package com.example.swp.service.Impl;

import com.example.swp.entity.PatientReport;
import com.example.swp.entity.Session;
import com.example.swp.repository.PatientReportRepository;
import com.example.swp.service.PatientReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientReportServiceImpl implements PatientReportService {
    PatientReportRepository patientReportRepository;
    @Override
    public Optional<PatientReport> findBySession(Session session) {
        return patientReportRepository.findBySession(session);
    }

    @Override
    public PatientReport save(PatientReport patientReport) {
        return patientReportRepository.save(patientReport);
    }

    @Override
    public Optional<PatientReport> findById(Integer id) {
        return patientReportRepository.findById(id);
    }
}

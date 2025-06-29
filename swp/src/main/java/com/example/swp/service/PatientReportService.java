package com.example.swp.service;

import com.example.swp.entity.PatientReport;
import com.example.swp.entity.Session;

import java.util.Optional;

public interface PatientReportService {
    Optional<PatientReport> findBySession(Session session);
    PatientReport save(PatientReport patientReport);
    Optional<PatientReport> findById(Integer id);

}

package com.example.swp.repository;

import com.example.swp.entity.PatientReport;
import com.example.swp.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientReportRepository extends JpaRepository<PatientReport, Integer> {
    Optional<PatientReport> findBySession(Session session);
}

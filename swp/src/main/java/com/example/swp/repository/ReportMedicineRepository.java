package com.example.swp.repository;

import com.example.swp.entity.PatientReport;
import com.example.swp.entity.ReportMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMedicineRepository extends JpaRepository<ReportMedicine, Integer> {
    List<ReportMedicine> findByPatientReport(PatientReport patientReport);
    void deleteByPatientReport(PatientReport patientReport);
}

package com.example.swp.service.Impl;

import com.example.swp.entity.PatientReport;
import com.example.swp.entity.ReportMedicine;
import com.example.swp.repository.ReportMedicineRepository;
import com.example.swp.service.ReportMedicineService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportMedicineServiceImpl implements ReportMedicineService {
    ReportMedicineRepository reportMedicineRepository;
    @Override
    public List<ReportMedicine> findByPatientReport(PatientReport patientReport) {
        return reportMedicineRepository.findByPatientReport(patientReport);
    }

    @Override
    public ReportMedicine save(ReportMedicine reportMedicine) {
        return  reportMedicineRepository.save(reportMedicine);
    }

    @Override
    @Transactional
    public void deleteByPatientReport(PatientReport report) {
        reportMedicineRepository.deleteByPatientReport(report);
    }

}

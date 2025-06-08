package com.example.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ReportMedicineId.class)
@Table(name = "Report_Medicine")
public class ReportMedicine {
    @Id
    @ManyToOne
    @JoinColumn(name = "patient_report_id")
    private PatientReport patientReport;

    @Id
    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;

    private Integer price;

    @Column(length = 200)
    private String note;
}

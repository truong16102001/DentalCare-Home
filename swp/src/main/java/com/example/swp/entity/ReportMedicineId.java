package com.example.swp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportMedicineId implements Serializable {
    private Integer patientReport;
    private String medicine;
}

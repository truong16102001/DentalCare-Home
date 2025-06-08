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
@IdClass(PatientReportImageId.class)
@Table(name = "Patient_Report_Images")
public class PatientReportImage {
    @Id
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Id
    @ManyToOne
    @JoinColumn(name = "report_id")
    private PatientReport report;
}


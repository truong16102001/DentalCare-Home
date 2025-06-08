package com.example.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Patient_Report")
public class PatientReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientReportId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(length = 500)
    private String diagnosis;

    @Column(length = 500)
    private String treatmentMethod;

    @Column(length = 500)
    private String doctorNote;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;
}


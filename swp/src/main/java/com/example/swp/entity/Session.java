package com.example.swp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "schedule_id")
    private WorkingSchedule schedule;

    private LocalDate sessionDate;

    private  String status; // Not start, processing, ended

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL)
    private PatientReport patientReport;
}
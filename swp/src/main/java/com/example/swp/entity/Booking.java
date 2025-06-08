package com.example.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(length = 200)
    private String patientName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(length = 32)
    private String phoneNumber;

    @Column(length = 64)
    private String email;

    @Column(length = 200)
    private String address;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    @Column(length = 20)
    private String status;

    @Column(length = 500)
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;
}

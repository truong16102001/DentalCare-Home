package com.example.swp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
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
    @JsonManagedReference
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "updated_user_id")
    private User updatedUser;

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

    private LocalDate appointmentDate;

    @Column(length = 20)
    private String status;

    @Column(length = 500)
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;
}

package com.example.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceId;

    @OneToOne
    @JoinColumn(name = "booking_id", unique = true)
    private Booking booking;

    private BigDecimal serviceFee;

    private BigDecimal medicineFee;

    private BigDecimal totalAmount;

    private Boolean isPaid;

    private LocalDateTime paidTime;

    private LocalDateTime createdTime;
}


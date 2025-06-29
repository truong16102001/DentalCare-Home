package com.example.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceId;

    @OneToOne
    @JoinColumn(name = "session_id", unique = true)
    private Session session;

    private BigDecimal totalFee;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paidTime;

    private LocalDate createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
}

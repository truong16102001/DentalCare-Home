package com.example.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(InvoiceMedicineId.class)
@Table(name = "Invoice_Medicine_Detail")
public class InvoiceMedicineDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @Id
    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;

    private BigDecimal price;
}


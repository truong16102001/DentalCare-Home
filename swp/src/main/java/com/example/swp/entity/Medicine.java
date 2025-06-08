package com.example.swp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Medicines")
public class Medicine {
    @Id
    @Column(length = 255)
    private String medicineId;

    @Column(length = 200)
    private String name;

    @Column(length = 100)
    private String unit;

    private Integer price;

    private Integer remainingQuantity;

    @Column(length = 1000)
    private String ingredients;

    @Column(length = 200)
    private String manufacturer;
}

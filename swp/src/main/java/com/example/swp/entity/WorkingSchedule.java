package com.example.swp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Working_Schedules")
public class WorkingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date_working;

    private LocalTime checkinTime;

    private LocalTime checkoutTime;

    private  Boolean isWoring;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;
}

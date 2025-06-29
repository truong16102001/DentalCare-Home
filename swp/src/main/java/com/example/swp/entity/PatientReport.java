package com.example.swp.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

    @OneToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(length = 500)
    private String diagnosis;

    @Column(length = 500)
    private String treatmentMethod;

    @Column(length = 500)
    private String doctorNote;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @OneToMany(mappedBy = "patientReport", fetch = FetchType.LAZY)
    private List<ReportMedicine> reportMedicines;

}

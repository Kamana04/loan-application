package com.loanservice.loan_service.entity;

import com.loanservice.loan_service.domain.enums.ApplicationStatus;
import com.loanservice.loan_service.domain.enums.RiskBand;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "loan_applications")
public class LoanApplicationEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    private RiskBand riskBand;

    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emi;
    private BigDecimal totalPayable;

    @Lob
    private String rejectionReasons;

    private LocalDateTime createdAt;

}
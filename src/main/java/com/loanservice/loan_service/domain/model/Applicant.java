package com.loanservice.loan_service.domain.model;

import com.loanservice.loan_service.domain.enums.EmploymentType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class Applicant {

    @NotBlank
    String name;

    @Min(21)
    @Max(60)
    int age;

    @DecimalMin("0.01")
    BigDecimal monthlyIncome;

    @NotNull
    EmploymentType employmentType;

    @Min(300)
    @Max(900)
    int creditScore;
}

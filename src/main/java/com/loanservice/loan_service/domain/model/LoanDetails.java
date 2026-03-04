package com.loanservice.loan_service.domain.model;

import com.loanservice.loan_service.domain.enums.LoanPurpose;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class LoanDetails {

    @DecimalMin("10000")
    @DecimalMax("5000000")
    private BigDecimal amount;

    @Min(6)
    @Max(360)
    private int tenureMonths;

    @NotNull
    private LoanPurpose purpose;
}

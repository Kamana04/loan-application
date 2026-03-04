package com.loanservice.loan_service.domain.model;

import com.loanservice.loan_service.domain.enums.LoanPurpose;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
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

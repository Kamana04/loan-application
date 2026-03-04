package com.loanservice.loan_service.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanOffer {

    private BigDecimal interestRate;
    private int tenureMonths;
    private BigDecimal emi;
    private BigDecimal totalPayable;
}

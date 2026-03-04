package com.loanservice.loan_service.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanApplicationRequest {

    @Valid
    @NotNull
    private Applicant applicant;

    @Valid
    @NotNull
    private LoanDetails loanDetails;
}

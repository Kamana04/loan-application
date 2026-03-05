package com.loanservice.loan_service.domain.model;

import com.loanservice.loan_service.domain.enums.ApplicationStatus;
import com.loanservice.loan_service.domain.enums.RiskBand;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LoanApplicationResponse {

    private UUID applicationId;
    private ApplicationStatus applicationStatus;
    private RiskBand riskBand;
    private LoanOffer loanOffer;
    private List<String> rejectionReasons;
}

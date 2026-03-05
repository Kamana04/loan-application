package com.loanservice.loan_service.service;

import com.loanservice.loan_service.domain.enums.ApplicationStatus;
import com.loanservice.loan_service.domain.enums.EmploymentType;
import com.loanservice.loan_service.domain.enums.RiskBand;
import com.loanservice.loan_service.domain.model.*;
import com.loanservice.loan_service.repository.LoanApplicationRepository;
import com.loanservice.loan_service.util.EmiCalculator;
import com.loanservice.loan_service.entity.LoanApplicationEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoanApplicationService {
    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(12);

    private final EmiCalculator emiCalculator;
    private final LoanApplicationRepository repository;

    public LoanApplicationService(EmiCalculator emiCalculator,
                                 LoanApplicationRepository repository) {
        this.emiCalculator = emiCalculator;
        this.repository = repository;
    }

    public LoanApplicationResponse evaluate(LoanApplicationRequest request) {

        UUID id = UUID.randomUUID();
        Applicant applicant = request.getApplicant();
        LoanDetails loan = request.getLoanDetails();

        List<String> rejectionReasons = new ArrayList<>();

        if (applicant.getCreditScore() < 600)
            rejectionReasons.add("CREDIT_SCORE_BELOW_MINIMUM");

        int tenureYears = loan.getTenureMonths() / 12;
        if (applicant.getAge() + tenureYears > 65)
            rejectionReasons.add("AGE_TENURE_LIMIT_EXCEEDED");

        RiskBand riskBand = null;
        BigDecimal finalRate = null;
        BigDecimal emi = null;

        if (rejectionReasons.isEmpty()) {

            riskBand = classifyRisk(applicant.getCreditScore());
            finalRate = calculateInterestRate(riskBand, applicant, loan);

            emi = emiCalculator.calculateEmi(
                    loan.getAmount(),
                    finalRate,
                    loan.getTenureMonths());

            BigDecimal sixtyPercent =
                    applicant.getMonthlyIncome()
                            .multiply(BigDecimal.valueOf(0.60));

            if (emi.compareTo(sixtyPercent) > 0)
                rejectionReasons.add("EMI_EXCEEDS_60_PERCENT");
        }

        LoanApplicationResponse response;

        if (!rejectionReasons.isEmpty()) {

           response = new LoanApplicationResponse(
                   id,
                   ApplicationStatus.REJECTED,
                   null,
                   null,
                   rejectionReasons);
        } else {

            BigDecimal fiftyPercent =
                    applicant.getMonthlyIncome()
                            .multiply(BigDecimal.valueOf(0.50));

            if (emi.compareTo(fiftyPercent) > 0) {

                response = new LoanApplicationResponse(
                        id,
                        ApplicationStatus.REJECTED,
                        null,
                        null,
                        List.of("EMI_EXCEEDS_50_PERCENT"));
            } else {

                BigDecimal totalPayable =
                        emi.multiply(BigDecimal.valueOf(loan.getTenureMonths()))
                                .setScale(2, RoundingMode.HALF_UP);

                LoanOffer offer = new LoanOffer(finalRate,
                        loan.getTenureMonths(),
                        emi,
                        totalPayable);

                response = new LoanApplicationResponse(
                        id,
                        ApplicationStatus.APPROVED,
                        riskBand,
                        offer,
                        null);
            }
        }

        saveToDb(response);
        return response;
    }

    private void saveToDb(LoanApplicationResponse response) {

        LoanApplicationEntity entity = new LoanApplicationEntity();

        entity.setStatus(response.getApplicationStatus());
        entity.setRiskBand(response.getRiskBand());

        if (response.getLoanOffer() != null) {
            entity.setInterestRate(response.getLoanOffer().getInterestRate());
            entity.setTenureMonths(response.getLoanOffer().getTenureMonths());
            entity.setEmi(response.getLoanOffer().getEmi());
            entity.setTotalPayable(response.getLoanOffer().getTotalPayable());
        }

        if (response.getRejectionReasons() != null)
            entity.setRejectionReasons(String.join(",",
                    response.getRejectionReasons()));

        entity.setCreatedAt(LocalDateTime.now());

        repository.save(entity);
    }

    private RiskBand classifyRisk(int score) {
        if (score >= 750) return RiskBand.LOW;
        if (score >= 650) return RiskBand.MEDIUM;
        return RiskBand.HIGH;
    }

    private BigDecimal calculateInterestRate(RiskBand band,
                                             Applicant applicant,
                                             LoanDetails loan) {

        BigDecimal rate = BASE_RATE;

        switch (band) {
            case MEDIUM -> rate = rate.add(BigDecimal.valueOf(1.5));
            case HIGH -> rate = rate.add(BigDecimal.valueOf(3));
        }

        if (applicant.getEmploymentType() == EmploymentType.SELF_EMPLOYED)
            rate = rate.add(BigDecimal.ONE);

        if (loan.getAmount().compareTo(BigDecimal.valueOf(1000000)) > 0)
            rate = rate.add(BigDecimal.valueOf(0.5));

        return rate.setScale(2, RoundingMode.HALF_UP);
    }
}

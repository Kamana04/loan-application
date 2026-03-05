package com.loanservice.loan_service.unit;

import com.loanservice.loan_service.domain.enums.EmploymentType;
import com.loanservice.loan_service.domain.enums.LoanPurpose;
import com.loanservice.loan_service.domain.enums.RiskBand;
import com.loanservice.loan_service.domain.model.Applicant;
import com.loanservice.loan_service.domain.model.LoanApplicationRequest;
import com.loanservice.loan_service.domain.model.LoanApplicationResponse;
import com.loanservice.loan_service.domain.model.LoanDetails;
import com.loanservice.loan_service.repository.LoanApplicationRepository;
import com.loanservice.loan_service.service.LoanApplicationService;
import com.loanservice.loan_service.util.EmiCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanServiceRiskTest {
    private LoanApplicationService loanService;
    private LoanApplicationRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LoanApplicationRepository.class);
        loanService = new LoanApplicationService(new EmiCalculator(), repository);
    }

    @Test
    void shouldClassifyLowRisk() {

        LoanApplicationRequest request = buildRequest(780);

        LoanApplicationResponse response = loanService.evaluate(request);

        assertEquals(RiskBand.LOW, response.getRiskBand());
    }

    @Test
    void shouldClassifyMediumRisk() {

        LoanApplicationRequest request = buildRequest(700);

        LoanApplicationResponse response = loanService.evaluate(request);

        assertEquals(RiskBand.MEDIUM, response.getRiskBand());
    }

    @Test
    void shouldClassifyHighRisk() {

        LoanApplicationRequest request = buildRequest(620);

        LoanApplicationResponse response = loanService.evaluate(request);

        assertEquals(RiskBand.HIGH, response.getRiskBand());
    }

    private LoanApplicationRequest buildRequest(int creditScore) {

        Applicant applicant = new Applicant();
        applicant.setName("Test User");
        applicant.setAge(30);
        applicant.setMonthlyIncome(new BigDecimal("100000"));
        applicant.setEmploymentType(EmploymentType.SALARIED);
        applicant.setCreditScore(creditScore);

        LoanDetails loan = new LoanDetails();
        loan.setAmount(new BigDecimal("200000"));
        loan.setTenureMonths(24);
        loan.setPurpose(LoanPurpose.PERSONAL);

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setApplicant(applicant);
        request.setLoanDetails(loan);

        return request;
    }

}

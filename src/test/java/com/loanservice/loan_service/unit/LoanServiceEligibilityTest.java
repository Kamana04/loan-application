package com.loanservice.loan_service.unit;

import com.loanservice.loan_service.domain.enums.ApplicationStatus;
import com.loanservice.loan_service.domain.enums.EmploymentType;
import com.loanservice.loan_service.domain.enums.LoanPurpose;
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

import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceEligibilityTest {
    private LoanApplicationService service;

    @BeforeEach
    void setup() {
        service = new LoanApplicationService(
                new EmiCalculator(),
                Mockito.mock(LoanApplicationRepository.class)
        );
    }

    @Test
    void shouldRejectIfCreditScoreBelow600() {

        LoanApplicationRequest request = buildRequest(550);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.REJECTED, response.getApplicationStatus());
        assertTrue(response.getRejectionReasons()
                .contains("CREDIT_SCORE_BELOW_MINIMUM"));
    }

    @Test
    void shouldRejectIfAgePlusTenureExceedsLimit() {

        Applicant applicant = new Applicant();
        applicant.setName("Test");
        applicant.setAge(60);
        applicant.setMonthlyIncome(new BigDecimal("50000"));
        applicant.setEmploymentType(EmploymentType.SALARIED);
        applicant.setCreditScore(750);

        LoanDetails loan = new LoanDetails();
        loan.setAmount(new BigDecimal("200000"));
        loan.setTenureMonths(120); // 10 years

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setApplicant(applicant);
        request.setLoanDetails(loan);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.REJECTED, response.getApplicationStatus());
        assertTrue(response.getRejectionReasons()
                .contains("AGE_TENURE_LIMIT_EXCEEDED"));
    }

    @Test
    void shouldApproveValidApplication() {

        LoanApplicationRequest request = buildRequest(750);

        LoanApplicationResponse response = service.evaluate(request);

        assertEquals(ApplicationStatus.APPROVED, response.getApplicationStatus());
        assertNotNull(response.getLoanOffer());
    }

    private LoanApplicationRequest buildRequest(int creditScore) {

        Applicant applicant = new Applicant();
        applicant.setName("Test");
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

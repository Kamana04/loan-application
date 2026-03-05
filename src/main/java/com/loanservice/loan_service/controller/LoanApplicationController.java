package com.loanservice.loan_service.controller;

import com.loanservice.loan_service.domain.model.LoanApplicationRequest;
import com.loanservice.loan_service.domain.model.LoanApplicationResponse;
import com.loanservice.loan_service.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> createLoanApplication(
            @Valid @RequestBody LoanApplicationRequest loanApplicationRequest) {
        return ResponseEntity.ok(loanApplicationService.evaluate(loanApplicationRequest));
    }
}

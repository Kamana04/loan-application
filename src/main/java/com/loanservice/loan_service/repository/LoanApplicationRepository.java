package com.loanservice.loan_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.loan.entity.LoanApplicationEntity;

import java.util.UUID;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity, UUID> {
}

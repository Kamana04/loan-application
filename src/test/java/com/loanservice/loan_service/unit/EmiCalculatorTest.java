package com.loanservice.loan_service.unit;

import com.loanservice.loan_service.util.EmiCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmiCalculatorTest {
    private final EmiCalculator emiCalculator = new EmiCalculator();

    @Test
    void shouldCalculateEmiCorrectly() {

        BigDecimal principal = new BigDecimal("500000");
        BigDecimal annualRate = new BigDecimal("12");
        int tenureMonths = 36;

        BigDecimal emi = emiCalculator.calculateEmi(principal, annualRate, tenureMonths);

        BigDecimal expected = new BigDecimal("16607");

        BigDecimal difference = emi.subtract(expected).abs();

        assertTrue(difference.compareTo(new BigDecimal("5")) < 0);
    }

    @Test
    void emiShouldBeScaledToTwoDecimals() {

        BigDecimal principal = new BigDecimal("100000");
        BigDecimal annualRate = new BigDecimal("12");
        int tenureMonths = 12;

        BigDecimal emi = emiCalculator.calculateEmi(principal, annualRate, tenureMonths);

        assertEquals(2, emi.scale());
    }

}

package com.loanservice.loan_service.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Component
public class EmiCalculator {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private static final MathContext MC = new MathContext(10, RoundingMode.HALF_UP);

    public BigDecimal calculateEmi(BigDecimal principal, BigDecimal rate, int tenureMonths) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), MC)
                .divide(MONTHS_IN_YEAR, MC);
        BigDecimal onePlusRPowerN = BigDecimal.ONE.add(monthlyRate).pow(tenureMonths, MC);

        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowerN);
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}

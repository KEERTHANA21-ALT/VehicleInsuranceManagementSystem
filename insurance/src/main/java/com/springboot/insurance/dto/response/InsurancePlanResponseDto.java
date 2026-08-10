package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.PlanType;

public record InsurancePlanResponseDto(
        Long id,
        PlanType planType,
        double basePremium,
        double coverageAmount,
        double discountPercentage,
        boolean inspectionRequired,
        boolean isActive

) {
}

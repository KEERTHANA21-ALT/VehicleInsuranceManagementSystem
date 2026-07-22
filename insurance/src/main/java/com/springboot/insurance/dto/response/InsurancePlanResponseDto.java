package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.PlanType;

public record InsurancePlanResponseDto(
        PlanType planType,
        double basePremium,
        double coverageAmount,
        boolean inspectionRequired

) {
}

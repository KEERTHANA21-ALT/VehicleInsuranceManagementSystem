package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.enums.PolicyStatus;

import java.time.LocalDate;

public record PolicyResponseDto(

        Long id,
        String policyHolderName,
        String vehicleNumber,
        PlanType planType,
        Double premiumAmount,
        Double coverageAmount,
        PolicyStatus policyStatus,
        String policyNumber,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isActive


) {
}

package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.PlanType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InsurancePlanRequestDto(

        @NotNull
        PlanType planType,

        @Min(value = 1000, message = "Base premium must be greater than 1000")
        double basePremium,

        @Min(value = 1000, message = "Coverage Amount must be greater than 1000")
        double coverageAmount,

        @NotNull
        Boolean inspectionRequired
) {
}

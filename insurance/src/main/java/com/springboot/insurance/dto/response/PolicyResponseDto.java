package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.PolicyStatus;

import java.time.LocalDate;

public record PolicyResponseDto(
        LocalDate startDate,
        LocalDate endDate,
        PolicyStatus policyStatus,
        String policyNumber
) {
}

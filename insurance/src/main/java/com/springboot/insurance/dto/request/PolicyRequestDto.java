package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.PolicyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PolicyRequestDto(

        @NotNull(message = "This field should not be empty")
        LocalDate startDate,

        @NotNull(message = "This field should not be empty")
        LocalDate endDate,

        @NotNull
        PolicyStatus policyStatus,

        @NotBlank(message = "This field should not be empty")
        String policyNumber


) {
}

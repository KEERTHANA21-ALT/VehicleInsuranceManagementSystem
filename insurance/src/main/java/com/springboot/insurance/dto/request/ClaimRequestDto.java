package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.ClaimStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClaimRequestDto(

        @NotNull(message = "Policy ID is required")
        Long policyId,

        @NotBlank(message = "This field should not be empty")
        String claimReason,

        @NotBlank(message = "This field should not be empty")
        String claimRemarks


) {

}

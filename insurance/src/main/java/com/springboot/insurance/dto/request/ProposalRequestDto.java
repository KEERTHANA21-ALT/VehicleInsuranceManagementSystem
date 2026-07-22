package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.ProposalStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProposalRequestDto(

        @NotNull
        Long vehicleId,

        @NotNull
        Long insurancePlanId,

        @Min(value = 3000, message = "Base Premium amount must be greater than or equal to 3000")
        double basePremium,

        @Min(value = 0, message = "Base Premium amount must be greater than or equal to 1000")
        double discount,

        @NotNull
        ProposalStatus proposalStatus


) {
}

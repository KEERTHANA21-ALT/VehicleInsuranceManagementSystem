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

        List<Long> addonIds




) {
}

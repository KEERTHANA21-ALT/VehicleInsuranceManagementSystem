package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.ProposalStatus;

public record ProposalResponseForEmployeeDto(
        Long id,

        String policyHolderName,

        String vehicleNumber,

        String planType,

        double premiumAmount,

        ProposalStatus proposalStatus
) {
}

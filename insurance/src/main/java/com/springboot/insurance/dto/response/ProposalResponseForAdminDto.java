package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.ProposalStatus;

public record ProposalResponseForAdminDto(
        Long id,

        String policyHolderName,

        String vehicleNumber,

        String planType,

        double premiumAmount,

        ProposalStatus proposalStatus,

        String employeeName,

        Boolean isActive,

        Boolean policyCreated
) {
}

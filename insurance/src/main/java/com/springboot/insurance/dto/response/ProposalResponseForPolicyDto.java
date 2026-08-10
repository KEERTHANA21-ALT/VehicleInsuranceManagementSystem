package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.enums.ProposalStatus;

import java.time.Instant;

public record ProposalResponseForPolicyDto(

        Long id,
        String policyHolderName,
        String vehicleNumber,
        PlanType planType,
        double premiumAmount,
        double basePremium,
        double discount,
        double coverageAmount,
        ProposalStatus proposalStatus,
        Instant proposalDate

) {
}
package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.ProposalStatus;

import java.time.Instant;

public record ProposalResponseDto(
        double premiumAmount,
        double basePremium,
        double discount,
        ProposalStatus proposalStatus,
        Instant proposalDate
) {
}

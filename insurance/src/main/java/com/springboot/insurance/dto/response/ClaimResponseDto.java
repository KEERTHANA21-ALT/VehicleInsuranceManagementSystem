package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.ClaimStatus;

import java.time.Instant;

public record ClaimResponseDto(
        double claimAmount,
        ClaimStatus claimStatus,
        Instant claimDate,
        String claimReason,
        String claimRemarks
) {
}

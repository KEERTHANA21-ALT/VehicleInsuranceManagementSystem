package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.ClaimStatus;

import java.time.Instant;

public record ClaimResponseDto(

        Long id,
        Double claimAmount,
        ClaimStatus claimStatus,
        Instant claimDate,
        String claimReason,
        String claimRemarks,
        Long policyId,
        String policyNumber,
        String vehicleNumber,
        String imageUrl


) {
}

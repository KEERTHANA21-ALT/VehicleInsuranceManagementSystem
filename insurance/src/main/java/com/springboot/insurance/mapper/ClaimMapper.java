package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.model.Claim;
import org.springframework.stereotype.Component;

@Component
public class ClaimMapper {
    public static Claim convertDtoToEntity(ClaimRequestDto dto) {

        Claim claim = new Claim();
        claim.setClaimAmount(dto.claimAmount());
        claim.setClaimStatus(dto.claimStatus());
        claim.setClaimReason(dto.claimReason());
        claim.setClaimRemarks(dto.claimRemarks());

        return claim;
    }

    public static ClaimResponseDto convertEntityToDto(Claim claim) {

        ClaimResponseDto claimResponseDto = new ClaimResponseDto(
                claim.getClaimAmount(),
                claim.getClaimStatus(),
                claim.getClaimDate(),
                claim.getClaimReason(),
                claim.getClaimRemarks()
        );
        return claimResponseDto;
    }
}

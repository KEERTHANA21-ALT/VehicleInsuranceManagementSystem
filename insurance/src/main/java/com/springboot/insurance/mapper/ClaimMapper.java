package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.model.Claim;
import org.springframework.stereotype.Component;

@Component
public class ClaimMapper {

    public static Claim convertDtoToEntity(ClaimRequestDto dto) {

        Claim claim = new Claim();


        claim.setClaimReason(dto.claimReason());
        claim.setClaimRemarks(dto.claimRemarks());


        return claim;
    }


    public static ClaimResponseDto convertEntityToDto(Claim claim) {

        return new ClaimResponseDto(
                claim.getId(),
                claim.getClaimAmount(),
                claim.getClaimStatus(),
                claim.getClaimDate(),
                claim.getClaimReason(),
                claim.getClaimRemarks(),
                claim.getPolicy().getId(),
                claim.getPolicy().getPolicyNumber(),
                claim.getPolicy().getProposal().getVehicle().getVehicleNumber(),
                claim.getImageUrl()
        );
    }
}
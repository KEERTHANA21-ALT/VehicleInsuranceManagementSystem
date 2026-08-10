package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.ProposalStatus;

public record ProposalStatusRequestDto(

        ProposalStatus proposalStatus
) {
}

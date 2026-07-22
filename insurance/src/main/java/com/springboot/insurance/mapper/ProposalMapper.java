package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.ProposalRequestDto;
import com.springboot.insurance.dto.response.ProposalResponseDto;
import com.springboot.insurance.enums.ProposalStatus;
import com.springboot.insurance.model.Proposal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;


@Component
public class ProposalMapper {

    public static Proposal convertDtoToEntity(double basePremium, double discount, ProposalStatus proposalStatus) {

        Proposal proposal = new Proposal();

        proposal.setBasePremium(basePremium);
        proposal.setDiscount(discount);
        proposal.setProposalStatus(proposalStatus);

        return proposal;
    }


    public static ProposalResponseDto convertEntityToDto(Proposal proposal) {
        ProposalResponseDto proposalResponseDto = new ProposalResponseDto(
                proposal.getPremiumAmount(),
                proposal.getBasePremium(),
                proposal.getDiscount(),
                proposal.getProposalStatus(),
                proposal.getProposalDate()
        );
        return proposalResponseDto;
    }
}

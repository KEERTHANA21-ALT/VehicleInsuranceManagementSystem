package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.response.ProposalAddonResponseDto;
import com.springboot.insurance.model.Addon;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.model.ProposalAddon;
import org.springframework.stereotype.Component;


@Component
public class ProposalAddonMapper {
    public static ProposalAddon convertToEntity(Proposal proposal, Addon addon) {

        ProposalAddon proposalAddon = new ProposalAddon();

        proposalAddon.setProposal(proposal);
        proposalAddon.setAddon(addon);
        proposalAddon.setAddonPrice(addon.getPrice());

        return proposalAddon;
    }

    public static ProposalAddonResponseDto convertEntityToDto(ProposalAddon proposalAddon) {
        ProposalAddonResponseDto dto = new ProposalAddonResponseDto(
                proposalAddon.getAddonPrice()
        );
        return dto;
    }
}

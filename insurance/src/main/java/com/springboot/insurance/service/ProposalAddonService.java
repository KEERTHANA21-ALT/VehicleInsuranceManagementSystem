package com.springboot.insurance.service;


import com.springboot.insurance.dto.response.ProposalAddonResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.ProposalAddonMapper;
import com.springboot.insurance.model.Addon;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.model.ProposalAddon;
import com.springboot.insurance.repository.AddonRepository;
import com.springboot.insurance.repository.ProposalAddonRepository;
import com.springboot.insurance.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProposalAddonService {

    private final ProposalAddonRepository proposalAddonRepository;
    private final ProposalRepository proposalRepository;
    private final AddonRepository addonRepository;


    public void add(Long proposalId, Long addonId, String username){

        // Fetch Proposal
        Proposal proposal = proposalRepository.findById(proposalId)
                        .orElseThrow(() -> new ResourceNotFoundException("Proposal Id is invalid"));


        // Fetch Addon
        Addon addon = addonRepository.findById(addonId)
                        .orElseThrow(() -> new ResourceNotFoundException("Addon Id is invalid"));


        // Create ProposalAddon
        ProposalAddon proposalAddon = ProposalAddonMapper.convertToEntity(
                        proposal,
                        addon
                );


        // Save relation
        proposalAddonRepository.save(proposalAddon);


        // Update premium
        proposal.setPremiumAmount(proposal.getPremiumAmount() + addon.getPrice());

        proposalAddon.setActive(true);

        proposalRepository.save(proposal);

    }

    public ProposalAddonResponseDto getById(Long proposalId,Long addonId,String username) {

        ProposalAddon proposalAddon = proposalAddonRepository.findByProposal_IdAndAddon_IdAndProposal_PolicyHolder_User_Username(proposalId,addonId,username)
                .orElseThrow(()->new ResourceNotFoundException("Proposal id Invalid"));

        return ProposalAddonMapper.convertEntityToDto(proposalAddon);
    }

    public void delete(long id) {
        ProposalAddon proposalAddon = proposalAddonRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Proposal Id invalid"));

        proposalAddon.setActive(false);

        proposalAddonRepository.save(proposalAddon);
    }
}

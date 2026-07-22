package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.PolicyRequestDto;
import com.springboot.insurance.dto.response.PolicyResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.PolicyMapper;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.repository.PolicyRepository;
import com.springboot.insurance.repository.ProposalRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final ProposalRepository proposalRepository;

    public void add(long proposalId,@Valid PolicyRequestDto dto) {

        // Step 1: Fetching proposal id
        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(()-> new ResourceNotFoundException("Proposal id Invalid"));

        // Step 2: Convert dto to Entity
        Policy policy = PolicyMapper.convertDtoToEntity(dto);

        // Step 3: Attach proposal id to policy
        policy.setProposal(proposal);

        // Step 4: Save policy in db
        policyRepository.save(policy);
    }

    public PolicyResponseDto getById(String username) {
        Policy policy = policyRepository.findByProposalPolicyHolderUserUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Policy not Invalid"));

        return PolicyMapper.convertEntityToDto(policy);
    }

    public List<PolicyResponseDto> getAll(String username) {
        List<Policy> list = policyRepository.findAllByProposalPolicyHolderUserUsername(username);

        return list
                .stream()
                .map(PolicyMapper :: convertEntityToDto)
                .toList();
    }
}

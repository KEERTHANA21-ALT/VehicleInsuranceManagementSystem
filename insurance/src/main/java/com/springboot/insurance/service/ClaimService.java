package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.ClaimMapper;
import com.springboot.insurance.mapper.PolicyMapper;
import com.springboot.insurance.model.Claim;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.repository.ClaimRepository;
import com.springboot.insurance.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;

    public void add(long policyId, ClaimRequestDto dto) {

        // Step 1: Fetch policy details
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(()->new ResourceNotFoundException("Policy id invalid"));

        // Step 2: convert dto to entity
        Claim claim = ClaimMapper.convertDtoToEntity(dto);

        // Step 3: attach policy to claim
        claim.setPolicy(policy);

        // Step 4: save in db
        claimRepository.save(claim);
    }

    public ClaimResponseDto getById(long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Claim is Invalid"));

        return ClaimMapper.convertEntityToDto(claim);
    }

    public List<ClaimResponseDto> getByPolicyId(long policyId) {
        List<Claim> list = claimRepository.getByPolicyId(policyId);

        return list
                .stream()
                .map(ClaimMapper :: convertEntityToDto)
                .toList();

    }
}

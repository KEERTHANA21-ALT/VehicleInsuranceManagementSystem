package com.springboot.insurance.repository;

import com.springboot.insurance.model.Claim;
import com.springboot.insurance.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    // Get all claims belonging to a policy holder
    List<Claim> findByPolicyProposalPolicyHolderUserUsername(String username);

    // Get one claim belonging to a policy holder
    Optional<Claim> findFirstByPolicyProposalPolicyHolderUserUsername(String username);

    // Get claims of a particular policy
    List<Claim> findByPolicyId(long policyId);

    // Get claims based on status
    // Used by Surveyor, Claim Manager and Insurance Manager
    List<Claim> findByClaimStatus(ClaimStatus claimStatus);


    List<Claim> findByClaimStatusIn(List<ClaimStatus> status);
}


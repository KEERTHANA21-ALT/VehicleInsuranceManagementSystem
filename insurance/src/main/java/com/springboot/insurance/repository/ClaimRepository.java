package com.springboot.insurance.repository;

import com.springboot.insurance.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim,Long> {



    List<Claim> findByPolicyProposalPolicyHolderUserUsername(String username);

    Optional<Claim> findFirstByPolicyProposalPolicyHolderUserUsername(String username);
}

package com.springboot.insurance.repository;

import com.springboot.insurance.model.ProposalAddon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProposalAddonRepository extends JpaRepository<ProposalAddon,Long> {

    Optional<ProposalAddon> findByProposal_IdAndAddon_IdAndProposal_PolicyHolder_User_Username(Long proposalId, Long addonId, String username);
}

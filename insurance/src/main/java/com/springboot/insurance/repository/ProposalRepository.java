package com.springboot.insurance.repository;

import com.springboot.insurance.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal,Long> {

    List<Proposal> findAllByPolicyHolderUserUsername(String username);

    Optional<Proposal> findByPolicyHolderUserUsername(String username);
}

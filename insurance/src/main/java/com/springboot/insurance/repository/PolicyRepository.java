package com.springboot.insurance.repository;

import com.springboot.insurance.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy,Long> {

    Optional<Policy> findByProposalPolicyHolderUserUsername(String username);

    List<Policy> findAllByProposalPolicyHolderUserUsername(String username);

    List<Policy> findAllByProposalEmployeeUserUsername(String username);

    Optional<Policy> findByIdAndProposalPolicyHolderUserUsername(long id, String username);
}

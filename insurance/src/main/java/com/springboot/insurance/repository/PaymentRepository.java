package com.springboot.insurance.repository;

import com.springboot.insurance.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findByProposalId(Long proposalId);

    Optional<Payment> findByProposalPolicyHolderUserUsername(String username);
}

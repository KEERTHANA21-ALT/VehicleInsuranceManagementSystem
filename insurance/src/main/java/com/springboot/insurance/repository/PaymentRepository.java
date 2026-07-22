package com.springboot.insurance.repository;

import com.springboot.insurance.model.Payment;
import com.springboot.insurance.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findByProposalId(Long proposalId);
}

package com.springboot.insurance.repository;

import com.springboot.insurance.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim,Long> {
    List<Claim> getByPolicyId(long policyId);
}

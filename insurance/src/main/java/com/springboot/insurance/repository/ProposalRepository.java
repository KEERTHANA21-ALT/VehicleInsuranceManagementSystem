package com.springboot.insurance.repository;

import com.springboot.insurance.enums.ProposalStatus;
import com.springboot.insurance.model.Employee;
import com.springboot.insurance.model.Proposal;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal,Long> {

    List<Proposal> findAllByPolicyHolderUserUsername(String username);

    Optional<Proposal> findByPolicyHolderUserUsername(String username);

    List<Proposal> findByEmployeeId(Long id);

    List<Proposal> findByEmployeeAndProposalStatus(Employee employee, ProposalStatus proposalStatus);

    Optional<Proposal> findByVehicleIdAndPolicyHolderIdAndProposalStatusNot(@NotNull Long aLong, Long id, ProposalStatus proposalStatus);
}

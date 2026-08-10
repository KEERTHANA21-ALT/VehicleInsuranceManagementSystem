package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.ProposalRequestDto;
import com.springboot.insurance.dto.request.ProposalStatusRequestDto;
import com.springboot.insurance.dto.response.ProposalResponseDto;
import com.springboot.insurance.dto.response.ProposalResponseForAdminDto;
import com.springboot.insurance.dto.response.ProposalResponseForEmployeeDto;
import com.springboot.insurance.dto.response.ProposalResponseForPolicyDto;
import com.springboot.insurance.enums.ProposalStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.ProposalMapper;
import com.springboot.insurance.model.*;
import com.springboot.insurance.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final PolicyHolderRepository policyHolderRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;
    private final InsurancePlanRepository insurancePlanRepository;
    private final UserRepository userRepository;

    public ProposalResponseDto add(String username, ProposalRequestDto dto) {

        PolicyHolder policyHolder = policyHolderRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Policy Holder not found"));

        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        InsurancePlan insurancePlan = insurancePlanRepository.findById(dto.insurancePlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Insurance Plan not found"));

        // Check whether this vehicle already has a proposal
        Proposal exists = proposalRepository.findByVehicleIdAndPolicyHolderIdAndProposalStatusNot(
                                dto.vehicleId(),
                                policyHolder.getId(),
                                ProposalStatus.REJECTED
                        )
                        .orElse(null);

        if (exists != null) {
            throw new ResourceNotFoundException("A proposal already exists for this vehicle");
        }


        Proposal proposal = new Proposal();

        proposal.setPolicyHolder(policyHolder);
        proposal.setVehicle(vehicle);
        proposal.setInsurancePlan(insurancePlan);

        // Base Premium from Insurance Plan
        double basePremium = insurancePlan.getBasePremium();

        // Discount calculated from Insurance Plan
        double discount = (basePremium * insurancePlan.getDiscountPercentage()) / 100;

        // Final Premium
        double premiumAmount = basePremium - discount;

        proposal.setBasePremium(basePremium);
        proposal.setDiscount(discount);
        proposal.setPremiumAmount(premiumAmount);

        proposal.setProposalStatus(ProposalStatus.INSPECTION_PENDING);

        proposal.setActive(true);
        proposal.setEmployee(null);

        Proposal savedProposal = proposalRepository.save(proposal);

        return ProposalMapper.convertEntityToDto(savedProposal);
    }

    public ProposalResponseDto getById(long proposalId) {

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(()-> new ResourceNotFoundException("Proposal Id is invalid"));

        return ProposalMapper.convertEntityToDto(proposal);
    }

    public List<ProposalResponseDto> getAll(String username) {

        List<Proposal> list = proposalRepository.findAllByPolicyHolderUserUsername(username);

        return list
                .stream()
                .map(ProposalMapper :: convertEntityToDto)
                .toList();
    }

    public void assignEmployee(long proposalId, long employeeId) {

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(() -> new ResourceNotFoundException("Proposal not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        proposal.setEmployee(employee);

        proposalRepository.save(proposal);
    }

    public void delete(long id) {
        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Proposal Id invalid"));
        
        proposal.setActive(false);

        proposalRepository.save(proposal);
    }

    public void update(long id, @Valid ProposalStatusRequestDto dto) {

        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Proposal Id invalid"));

        proposal.setProposalStatus(dto.proposalStatus());

        proposalRepository.save(proposal);

    }

    public List<ProposalResponseForAdminDto> getAllForAdmin() {

        List<Proposal> list = proposalRepository.findAll();
        return list
                .stream()
                .map(ProposalMapper :: convertEntityToDtoForAdmin)
                .toList();
    }

    public List<ProposalResponseForAdminDto> getEmployeeProposals(String username) {

        Employee employee = employeeRepository.findByUserUsername(username)
                        .orElseThrow(()-> new RuntimeException("Employee not found"));


        List<Proposal> proposals = proposalRepository.findByEmployeeId(employee.getId());


        return proposals
                .stream()
                .map(ProposalMapper::convertEntityToDtoForAdmin)
                .toList();
    }


    public List<ProposalResponseForEmployeeDto> getApprovedProposals(String username) {

        Employee employee = employeeRepository.findAllByUserUsername(username);

        List<Proposal> list = proposalRepository.findByEmployeeAndProposalStatus(employee, ProposalStatus.APPROVED);

        return list
                .stream()
                .map(ProposalMapper :: convertEntityToDtoForEmployee)
                .toList();




    }

    public ProposalResponseForPolicyDto getByIdForPolicy(long proposalId) {

        Proposal proposal = proposalRepository.findById(proposalId)
                .orElseThrow(()-> new ResourceNotFoundException("Proposal Id is invalid"));

        return ProposalMapper.convertEntityToDtoForPolicy(proposal);


    }
}

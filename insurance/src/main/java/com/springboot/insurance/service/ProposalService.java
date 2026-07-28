package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.ProposalRequestDto;
import com.springboot.insurance.dto.response.ProposalResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.ProposalMapper;
import com.springboot.insurance.model.*;
import com.springboot.insurance.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final PolicyHolderRepository policyHolderRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;
    private final InsurancePlanRepository insurancePlanRepository;
    private final UserRepository userRepository;

    public void add(String username,ProposalRequestDto dto) {

//         Step 1: Fetch policyHolderDetails
        PolicyHolder policyHolder = policyHolderRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("PolicyHolder Id is invalid"));

//         Step 2: Fetch VehicleDetails
        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(()->new ResourceNotFoundException("Vehicle Id is invalid"));

//         Step 3: Fetch InsurancePlanDetails
        InsurancePlan insurancePlan =  insurancePlanRepository.findById(dto.insurancePlanId())
                .orElseThrow(()->new ResourceNotFoundException("InsurancePlan Id is invalid"));

        // Step 4: Fetch proposal details from dto
        Proposal proposal = ProposalMapper.convertDtoToEntity(
                dto.basePremium(),
                dto.discount(),
                dto.proposalStatus()
        );

        double premiumAmount = proposal.getBasePremium() - proposal.getDiscount();
        proposal.setPremiumAmount(premiumAmount);

        // Step 5: Attach values
        proposal.setPolicyHolder(policyHolder);
        proposal.setVehicle(vehicle);
        proposal.setInsurancePlan(insurancePlan);

        // Step 6: employee assigned later like surveyor all
        proposal.setEmployee(null);

        proposal.setActive(true);

        // Step 7: Save in Db
        proposalRepository.save(proposal);

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

    public void update(long id, @Valid ProposalRequestDto dto) {

        Proposal proposal = proposalRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Proposal Id invalid"));

        proposal.setProposalStatus(dto.proposalStatus());

        proposalRepository.save(proposal);

    }
}

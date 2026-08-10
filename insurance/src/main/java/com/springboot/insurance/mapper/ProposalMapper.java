package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.ProposalRequestDto;
import com.springboot.insurance.dto.response.ProposalResponseDto;
import com.springboot.insurance.dto.response.ProposalResponseForAdminDto;
import com.springboot.insurance.dto.response.ProposalResponseForEmployeeDto;
import com.springboot.insurance.dto.response.ProposalResponseForPolicyDto;
import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.enums.ProposalStatus;
import com.springboot.insurance.model.Proposal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.time.Instant;


@Component
public class ProposalMapper {

//    public static Proposal convertDtoToEntity(double basePremium, double discount, ProposalStatus proposalStatus) {
//
//        Proposal proposal = new Proposal();
//
//        proposal.setBasePremium(basePremium);
//        proposal.setDiscount(discount);
//        proposal.setProposalStatus(proposalStatus);
//
//        return proposal;
//    }


    public static ProposalResponseDto convertEntityToDto(Proposal proposal) {
        ProposalResponseDto proposalResponseDto = new ProposalResponseDto(
                proposal.getId(),
                proposal.getPremiumAmount(),
                proposal.getBasePremium(),
                proposal.getDiscount(),
                proposal.getProposalStatus(),
                proposal.getProposalDate()
        );
        return proposalResponseDto;
    }

    public static ProposalResponseForAdminDto convertEntityToDtoForAdmin(Proposal proposal) {
        ProposalResponseForAdminDto dto = new ProposalResponseForAdminDto(
                proposal.getId(),
                proposal.getPolicyHolder().getName(),
                proposal.getVehicle().getVehicleNumber(),
                proposal.getInsurancePlan().getPlanType().toString(),
                proposal.getPremiumAmount(),
                proposal.getProposalStatus(),
                proposal.getEmployee() != null
                        ? proposal.getEmployee().getName()
                        : "Not Assigned",

                proposal.isActive(),
                proposal.isPolicyCreated()

        );
        return dto;
    }

    public static ProposalResponseForEmployeeDto convertEntityToDtoForEmployee(Proposal proposal) {

        return new ProposalResponseForEmployeeDto(

                proposal.getId(),
                proposal.getPolicyHolder().getName(),
                proposal.getVehicle().getVehicleNumber(),
                proposal.getInsurancePlan().getPlanType().name(),
                proposal.getPremiumAmount(),
                proposal.getProposalStatus()

        );

    }

    public static ProposalResponseForPolicyDto convertEntityToDtoForPolicy(Proposal proposal) {


        ProposalResponseForPolicyDto dto = new ProposalResponseForPolicyDto(
                proposal.getId(),
                proposal.getPolicyHolder().getName(),
                proposal.getVehicle().getVehicleNumber(),
                proposal.getInsurancePlan().getPlanType(),
                proposal.getPremiumAmount(),
                proposal.getBasePremium(),
                proposal.getDiscount(),
                proposal.getInsurancePlan().getCoverageAmount(),
                proposal.getProposalStatus(),
                proposal.getProposalDate()
        );
        return dto;


    }
}

package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.PolicyRequestDto;
import com.springboot.insurance.dto.response.PolicyResponseDto;
import com.springboot.insurance.model.Policy;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class PolicyMapper {
    public static Policy convertDtoToEntity(@Valid PolicyRequestDto dto) {

        Policy policy = new Policy();

        policy.setStartDate(dto.startDate());
        policy.setEndDate(dto.endDate());
        policy.setPolicyStatus(dto.policyStatus());
        policy.setPolicyNumber(dto.policyNumber());

        return policy;
    }

    public static PolicyResponseDto convertEntityToDto(Policy policy) {

        PolicyResponseDto policyResponseDto = new PolicyResponseDto(
                policy.getId(),
                policy.getProposal().getPolicyHolder().getName(),
                policy.getProposal().getVehicle().getVehicleNumber(),
                policy.getProposal().getInsurancePlan().getPlanType(),
                policy.getProposal().getPremiumAmount(),
                policy.getProposal().getInsurancePlan().getCoverageAmount(),
                policy.getPolicyStatus(),
                policy.getPolicyNumber(),
                policy.getStartDate(),
                policy.getEndDate(),
                policy.isActive()

        );
        return policyResponseDto;
    }
}

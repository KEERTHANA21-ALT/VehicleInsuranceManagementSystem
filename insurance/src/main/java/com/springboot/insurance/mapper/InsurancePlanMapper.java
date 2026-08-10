package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.InsurancePlanRequestDto;
import com.springboot.insurance.dto.response.InsurancePlanResponseDto;
import com.springboot.insurance.model.InsurancePlan;
import org.springframework.stereotype.Component;

@Component
public class InsurancePlanMapper {

    public static InsurancePlan convertDtoToEntity(InsurancePlanRequestDto dto) {

        InsurancePlan insurancePlan = new InsurancePlan();
        insurancePlan.setPlanType(dto.planType());
        insurancePlan.setBasePremium(dto.basePremium());
        insurancePlan.setCoverageAmount(dto.coverageAmount());
        insurancePlan.setInspectionRequired(dto.inspectionRequired());
        insurancePlan.setDiscountPercentage(dto.discountPercentage());

        return insurancePlan;
    }

    public static InsurancePlanResponseDto convertEntityToDto(InsurancePlan insurancePlan) {

        InsurancePlanResponseDto insurancePlanResponseDto = new InsurancePlanResponseDto(
                insurancePlan.getId(),
                insurancePlan.getPlanType(),
                insurancePlan.getBasePremium(),
                insurancePlan.getCoverageAmount(),
                insurancePlan.getDiscountPercentage(),
                insurancePlan.isInspectionRequired(),
                insurancePlan.isActive()
        );
        return insurancePlanResponseDto;
    }
}

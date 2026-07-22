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

        return insurancePlan;
    }

    public static InsurancePlanResponseDto convertEntityToDto(InsurancePlan insurancePlan) {

        InsurancePlanResponseDto insurancePlanResponseDto = new InsurancePlanResponseDto(
                insurancePlan.getPlanType(),
                insurancePlan.getBasePremium(),
                insurancePlan.getCoverageAmount(),
                insurancePlan.isInspectionRequired()
        );
        return insurancePlanResponseDto;
    }
}

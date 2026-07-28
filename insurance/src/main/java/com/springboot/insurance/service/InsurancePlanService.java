package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.InsurancePlanRequestDto;
import com.springboot.insurance.dto.response.InsurancePlanResponseDto;
import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.InsurancePlanMapper;
import com.springboot.insurance.model.InsurancePlan;
import com.springboot.insurance.repository.InsurancePlanRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsurancePlanService {

    private final InsurancePlanRepository insurancePlanRepository;

    public void add(InsurancePlanRequestDto dto) {

        // Step 1: Convert dto to Entity
        InsurancePlan insurancePlan = InsurancePlanMapper.convertDtoToEntity(dto);

        insurancePlan.setActive(true);

        // Step 2: Save Entity in Db
        insurancePlanRepository.save(insurancePlan);
    }

    public InsurancePlanResponseDto getById(long id) {
        InsurancePlan insurancePlan = insurancePlanRepository.fetchById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Insurance Plan Id invalid"));

        return InsurancePlanMapper.convertEntityToDto(insurancePlan);
    }

    public List<InsurancePlanResponseDto> getByPlanType(PlanType planType) {
        List<InsurancePlan> list = insurancePlanRepository.getByPlanType(planType);

        return list
                .stream()
                .map(InsurancePlanMapper :: convertEntityToDto)
                .toList();
    }

    public void delete(long id) {

        InsurancePlan insurancePlan = insurancePlanRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Insurance Plan Id invalid"));

        insurancePlan.setActive(false);

        insurancePlanRepository.save(insurancePlan);
    }

    public void update(long id, @Valid InsurancePlanRequestDto dto) {

        InsurancePlan insurancePlan = insurancePlanRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Insurance Plan Id invalid"));

        insurancePlan.setPlanType(dto.planType());
        insurancePlan.setBasePremium(dto.basePremium());
        insurancePlan.setCoverageAmount(dto.coverageAmount());
        insurancePlan.setInspectionRequired(dto.inspectionRequired());

        insurancePlanRepository.save(insurancePlan);

    }
}

package com.springboot.insurance.service;


import com.springboot.insurance.dto.request.InsurancePlanRequestDto;
import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.InsurancePlan;
import com.springboot.insurance.repository.InsurancePlanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InsurancePlanTest {

    @InjectMocks
    private InsurancePlanService insurancePlanService;

    @Mock
    private InsurancePlanRepository insurancePlanRepository;

    private InsurancePlan insurancePlan1;
    private InsurancePlan insurancePlan2;

    @BeforeEach
    public void init() {

        insurancePlan1 = new InsurancePlan(
                1L,
                PlanType.COMPREHENSIVE,
                5000,
                500000,
                true,
                true
        );

        insurancePlan2 = new InsurancePlan(
                2L,
                PlanType.THIRD_PARTY,
                2500,
                100000,
                false,
                true
        );
    }

    @Test
    public void addTest() {

        InsurancePlanRequestDto dto = new InsurancePlanRequestDto(
                PlanType.COMPREHENSIVE,
                5000,
                500000,
                true
        );

        insurancePlanService.add(dto);

        ArgumentCaptor<InsurancePlan> captor =
                ArgumentCaptor.forClass(InsurancePlan.class);

        verify(insurancePlanRepository, times(1))
                .save(captor.capture());

        Assertions.assertEquals(dto.planType(), captor.getValue().getPlanType());
        Assertions.assertEquals(dto.basePremium(), captor.getValue().getBasePremium());
        Assertions.assertEquals(dto.coverageAmount(), captor.getValue().getCoverageAmount());
        Assertions.assertEquals(dto.inspectionRequired(), captor.getValue().isInspectionRequired());
        Assertions.assertTrue(captor.getValue().isActive());
    }

    @Test
    public void getByIdPresent() {

        when(insurancePlanRepository.fetchById(1L)).thenReturn(Optional.of(insurancePlan1));

        Assertions.assertEquals(
                PlanType.COMPREHENSIVE,
                insurancePlanService.getById(1L).planType()
        );

        verify(insurancePlanRepository, times(1)).fetchById(1L);
    }

    @Test
    public void getByIdAbsent() {

        when(insurancePlanRepository.fetchById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Insurance Plan Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> insurancePlanService.getById(10L)
                ).getMessage()
        );

        verify(insurancePlanRepository, times(1)).fetchById(10L);
    }

    @Test
    public void getByPlanTypeTest() {

        List<InsurancePlan> list = List.of(insurancePlan1,insurancePlan2);

        when(insurancePlanRepository.getByPlanType(PlanType.COMPREHENSIVE)).thenReturn(list);

        Assertions.assertEquals(
                2,
                insurancePlanService.getByPlanType(PlanType.COMPREHENSIVE).size()
        );

        verify(insurancePlanRepository, times(1)).getByPlanType(PlanType.COMPREHENSIVE);
    }

    @Test
    public void deleteTest() {

        when(insurancePlanRepository.findById(1L)).thenReturn(Optional.of(insurancePlan1));

        when(insurancePlanRepository.save(insurancePlan1)).thenReturn(insurancePlan1);

        insurancePlanService.delete(1L);

        verify(insurancePlanRepository, times(1)).save(insurancePlan1);
    }

    @Test
    public void deleteInvalidId() {

        when(insurancePlanRepository.findById(5L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Insurance Plan Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> insurancePlanService.delete(5L)
                ).getMessage()
        );

        verify(insurancePlanRepository, never()).save(any());
    }

    @Test
    public void updateTest() {

        when(insurancePlanRepository.findById(1L))
                .thenReturn(Optional.of(insurancePlan1));

        InsurancePlanRequestDto dto = new InsurancePlanRequestDto(
                PlanType.THIRD_PARTY,
                3000,
                150000,
                false
        );

        insurancePlanService.update(1L, dto);

        verify(insurancePlanRepository, times(1)).findById(1L);
        verify(insurancePlanRepository, times(1)).save(insurancePlan1);
    }

    @Test
    public void updateInvalidId() {

        when(insurancePlanRepository.findById(10L)).thenReturn(Optional.empty());

        InsurancePlanRequestDto dto = new InsurancePlanRequestDto(
                PlanType.THIRD_PARTY,
                3000,
                150000,
                false
        );

        Assertions.assertEquals(
                "Insurance Plan Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> insurancePlanService.update(10L, dto)
                ).getMessage()
        );

        verify(insurancePlanRepository, times(1)).findById(10L);
        verify(insurancePlanRepository, times(0)).save(insurancePlan1);
    }
}

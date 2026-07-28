package com.springboot.insurance.service;


import com.springboot.insurance.dto.request.ProposalRequestDto;
import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.enums.ProposalStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.*;
import com.springboot.insurance.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProposalServiceTest {

    @InjectMocks
    private ProposalService proposalService;

    @Mock
    private ProposalRepository proposalRepository;

    @Mock
    private PolicyHolderRepository policyHolderRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private InsurancePlanRepository insurancePlanRepository;

    @Mock
    private UserRepository userRepository;

    private Proposal proposal1;
    private Proposal proposal2;

    private PolicyHolder policyHolder1;
    private Vehicle vehicle1;
    private InsurancePlan insurancePlan1;
    private Employee employee1;

    @BeforeEach
    public void init() {

        policyHolder1 = new PolicyHolder();
        policyHolder1.setId(1L);

        vehicle1 = new Vehicle();
        vehicle1.setId(1L);

        insurancePlan1 = new InsurancePlan();
        insurancePlan1.setId(1L);
        insurancePlan1.setPlanType(PlanType.COMPREHENSIVE);

        employee1 = new Employee();
        employee1.setId(1L);

        proposal1 = new Proposal(
                1L,
                4500,
                5000,
                500,
                true,
                ProposalStatus.REJECTED,
                Instant.now(),
                policyHolder1,
                employee1,
                vehicle1,
                insurancePlan1
        );

        proposal2 = new Proposal(
                2L,
                7000,
                8000,
                1000,
                true,
                ProposalStatus.APPROVED,
                Instant.now(),
                policyHolder1,
                employee1,
                vehicle1,
                insurancePlan1
        );
    }

    @Test
    public void addTest() {

        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(insurancePlanRepository.findById(1L)).thenReturn(Optional.of(insurancePlan1));

        ProposalRequestDto dto = new ProposalRequestDto(
                1L,
                1L,
                5000,
                500,
                ProposalStatus.REJECTED
        );

        proposalService.add("john@gmail.com", dto);

        ArgumentCaptor<Proposal> captor = ArgumentCaptor.forClass(Proposal.class);

        verify(proposalRepository).save(captor.capture());

        Assertions.assertEquals(dto.basePremium(), captor.getValue().getBasePremium());
        Assertions.assertEquals(dto.discount(), captor.getValue().getDiscount());
        Assertions.assertEquals(4500, captor.getValue().getPremiumAmount());
        Assertions.assertEquals(policyHolder1, captor.getValue().getPolicyHolder());
        Assertions.assertEquals(vehicle1, captor.getValue().getVehicle());
        Assertions.assertEquals(insurancePlan1, captor.getValue().getInsurancePlan());
        Assertions.assertTrue(captor.getValue().isActive());
    }

    @Test
    public void getByIdPresent() {

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));

        Assertions.assertEquals(
                ProposalStatus.REJECTED,
                proposalService.getById(1L).proposalStatus()
        );

        verify(proposalRepository).findById(1L);
    }

    @Test
    public void getByIdAbsent() {

        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Proposal Id is invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> proposalService.getById(10L)
                ).getMessage()
        );

        verify(proposalRepository).findById(10L);
    }

    @Test
    public void assignEmployeeTest() {

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        proposalService.assignEmployee(1L, 1L);

        verify(proposalRepository).findById(1L);
        verify(employeeRepository).findById(1L);
        verify(proposalRepository).save(proposal1);
    }

    @Test
    public void deleteTest() {

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));
        when(proposalRepository.save(proposal1)).thenReturn(proposal1);

        proposalService.delete(1L);

        verify(proposalRepository).save(proposal1);
    }

    @Test
    public void deleteInvalidId() {

        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Proposal Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> proposalService.delete(10L)
                ).getMessage()
        );

        verify(proposalRepository, never()).save(any());
    }

    @Test
    public void updateTest() {

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));

        ProposalRequestDto dto = new ProposalRequestDto(
                1L,
                1L,
                5000,
                500,
                ProposalStatus.APPROVED
        );

        proposalService.update(1L, dto);

        verify(proposalRepository).findById(1L);
        verify(proposalRepository).save(proposal1);
    }

    @Test
    public void updateInvalidId() {

        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        ProposalRequestDto dto = new ProposalRequestDto(
                1L,
                1L,
                5000,
                500,
                ProposalStatus.APPROVED
        );

        Assertions.assertEquals(
                "Proposal Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> proposalService.update(10L, dto)
                ).getMessage()
        );

        verify(proposalRepository).findById(10L);
        verify(proposalRepository, times(0)).save(proposal1);
    }
}


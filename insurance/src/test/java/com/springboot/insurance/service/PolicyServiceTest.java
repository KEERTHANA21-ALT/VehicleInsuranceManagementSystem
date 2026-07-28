package com.springboot.insurance.service;


import com.springboot.insurance.dto.request.PolicyRequestDto;
import com.springboot.insurance.enums.PolicyStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.repository.PolicyRepository;
import com.springboot.insurance.repository.ProposalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceTest {


    @InjectMocks
    private PolicyService policyService;

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private ProposalRepository proposalRepository;

    private Policy policy1;
    private Policy policy2;

    private Proposal proposal1;


    @BeforeEach
    public void init() {

        proposal1 = new Proposal();
        proposal1.setId(1L);

        policy1 = new Policy(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 1),
                true,
                PolicyStatus.ACTIVE,
                "POL1001",
                proposal1
        );

        policy2 = new Policy(
                2L,
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2027, 2, 1),
                true,
                PolicyStatus.EXPIRED,
                "POL1002",
                proposal1
        );
    }

    @Test
    public void addTest() {

        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));

        PolicyRequestDto dto = new PolicyRequestDto(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 1),
                PolicyStatus.ACTIVE,
                "POL1001"
        );

        policyService.add(1L, dto);

        ArgumentCaptor<Policy> captor = ArgumentCaptor.forClass(Policy.class);

        verify(policyRepository, times(1)).save(captor.capture());

        Assertions.assertEquals(dto.startDate(), captor.getValue().getStartDate());
        Assertions.assertEquals(dto.endDate(), captor.getValue().getEndDate());
        Assertions.assertEquals(dto.policyStatus(), captor.getValue().getPolicyStatus());
        Assertions.assertEquals(dto.policyNumber(), captor.getValue().getPolicyNumber());
        Assertions.assertEquals(proposal1, captor.getValue().getProposal());
        Assertions.assertTrue(captor.getValue().isActive());
    }

    @Test
    public void getByIdPresent() {

        when(policyRepository.findByProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.of(policy1));

        Assertions.assertEquals(
                "POL1001",
                policyService.getById("john@gmail.com").policyNumber()
        );

        verify(policyRepository, times(1)).findByProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getByIdAbsent() {

        when(policyRepository.findByProposalPolicyHolderUserUsername("john@gmail.com"))
                .thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Policy not Invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> policyService.getById("john@gmail.com")
                ).getMessage()
        );

        verify(policyRepository, times(1))
                .findByProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getAllMyPoliciesTest() {

        List<Policy> list = List.of(policy1, policy2);

        when(policyRepository.findAllByProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(list);

        Assertions.assertEquals(
                2,
                policyService.getAll("john@gmail.com").size()
        );

        verify(policyRepository, times(1)).findAllByProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void deleteTest() {

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy1));
        when(policyRepository.save(policy1)).thenReturn(policy1);

        policyService.delete(1L);

        verify(policyRepository, times(1)).save(policy1);
    }

    @Test
    public void deleteInvalidId() {

        when(policyRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Policy Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> policyService.delete(10L)
                ).getMessage()
        );

        verify(policyRepository, never()).save(any());
    }

    @Test
    public void updateTest() {

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy1));

        PolicyRequestDto dto = new PolicyRequestDto(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2027, 5, 1),
                PolicyStatus.EXPIRED,
                "POL2001"
        );

        policyService.update(1L, dto);

        verify(policyRepository, times(1)).findById(1L);
        verify(policyRepository, times(1)).save(policy1);
    }

    @Test
    public void updateInvalidId() {

        when(policyRepository.findById(10L)).thenReturn(Optional.empty());

        PolicyRequestDto dto = new PolicyRequestDto(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2027, 5, 1),
                PolicyStatus.ACTIVE,
                "POL2001"
        );

        Assertions.assertEquals(
                "Policy Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> policyService.update(10L, dto)
                ).getMessage()
        );

        verify(policyRepository, times(1)).findById(10L);
        verify(policyRepository, times(0)).save(policy1);
    }

}

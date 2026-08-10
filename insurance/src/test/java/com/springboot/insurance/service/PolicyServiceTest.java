package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.PolicyRequestDto;
import com.springboot.insurance.enums.PolicyStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.InsurancePlan;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.model.Vehicle;
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

    @InjectMocks private PolicyService policyService;
    @Mock private PolicyRepository policyRepository;
    @Mock private ProposalRepository proposalRepository;

    private Policy policy1;
    private Policy policy2;
    private Proposal proposal1;

    @BeforeEach
    public void init() {
        PolicyHolder policyHolder = new PolicyHolder();
        policyHolder.setName("John");

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("TN01AB1234");

        InsurancePlan insurancePlan = mock(InsurancePlan.class);

        proposal1 = new Proposal();
        proposal1.setId(1L);
        proposal1.setPolicyCreated(false);
        proposal1.setPolicyHolder(policyHolder);
        proposal1.setVehicle(vehicle);
        proposal1.setInsurancePlan(insurancePlan);

        policy1 = new Policy(1L, LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1),
                true, PolicyStatus.ACTIVE, "POL1001", proposal1);

        policy2 = new Policy(2L, LocalDate.of(2026, 2, 1), LocalDate.of(2027, 2, 1),
                true, PolicyStatus.EXPIRED, "POL1002", proposal1);
    }

    @Test
    public void addTest() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));

        PolicyRequestDto dto = new PolicyRequestDto(
                LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1),
                PolicyStatus.ACTIVE, "POL1001");

        policyService.add(1L, dto);

        ArgumentCaptor<Policy> captor = ArgumentCaptor.forClass(Policy.class);

        verify(proposalRepository).findById(1L);
        verify(policyRepository).save(captor.capture());

        Policy savedPolicy = captor.getValue();

        Assertions.assertEquals(dto.startDate(), savedPolicy.getStartDate());
        Assertions.assertEquals(dto.endDate(), savedPolicy.getEndDate());
        Assertions.assertEquals(dto.policyStatus(), savedPolicy.getPolicyStatus());
        Assertions.assertEquals(dto.policyNumber(), savedPolicy.getPolicyNumber());
        Assertions.assertEquals(proposal1, savedPolicy.getProposal());
        Assertions.assertTrue(savedPolicy.isActive());
        Assertions.assertTrue(proposal1.isPolicyCreated());
    }

    @Test
    public void addInvalidProposal() {
        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        PolicyRequestDto dto = new PolicyRequestDto(
                LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1),
                PolicyStatus.ACTIVE, "POL1001");

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> policyService.add(10L, dto));

        Assertions.assertEquals("Proposal id Invalid", exception.getMessage());
        verify(proposalRepository).findById(10L);
        verify(policyRepository, never()).save(any());
    }

    @Test
    public void getByIdPresent() {
        when(policyRepository.findByIdAndProposalPolicyHolderUserUsername(1L, "john@gmail.com"))
                .thenReturn(Optional.of(policy1));

        Assertions.assertEquals("POL1001",
                policyService.getById(1L, "john@gmail.com").policyNumber());

        verify(policyRepository).findByIdAndProposalPolicyHolderUserUsername(1L, "john@gmail.com");
    }

    @Test
    public void getByIdAbsent() {
        when(policyRepository.findByIdAndProposalPolicyHolderUserUsername(10L, "john@gmail.com"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> policyService.getById(10L, "john@gmail.com"));

        Assertions.assertEquals("Policy not Invalid", exception.getMessage());
        verify(policyRepository).findByIdAndProposalPolicyHolderUserUsername(10L, "john@gmail.com");
    }

    @Test
    public void getAllTest() {
        when(policyRepository.findAllByProposalPolicyHolderUserUsername("john@gmail.com"))
                .thenReturn(List.of(policy1, policy2));

        Assertions.assertEquals(2, policyService.getAll("john@gmail.com").size());

        verify(policyRepository).findAllByProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getAllEmptyTest() {
        when(policyRepository.findAllByProposalPolicyHolderUserUsername("john@gmail.com"))
                .thenReturn(List.of());

        Assertions.assertEquals(0, policyService.getAll("john@gmail.com").size());

        verify(policyRepository).findAllByProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void deleteTest() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy1));
        when(policyRepository.save(policy1)).thenReturn(policy1);

        policyService.delete(1L);

        Assertions.assertFalse(policy1.isActive());
        verify(policyRepository).findById(1L);
        verify(policyRepository).save(policy1);
    }

    @Test
    public void deleteInvalidId() {
        when(policyRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> policyService.delete(10L));

        Assertions.assertEquals("Policy Id invalid", exception.getMessage());
        verify(policyRepository).findById(10L);
        verify(policyRepository, never()).save(any());
    }

    @Test
    public void updateTest() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy1));

        PolicyRequestDto dto = new PolicyRequestDto(
                LocalDate.of(2026, 5, 1), LocalDate.of(2027, 5, 1),
                PolicyStatus.EXPIRED, "POL2001");

        policyService.update(1L, dto);

        Assertions.assertEquals(PolicyStatus.EXPIRED, policy1.getPolicyStatus());
        Assertions.assertEquals(LocalDate.of(2026, 1, 1), policy1.getStartDate());
        Assertions.assertEquals(LocalDate.of(2027, 1, 1), policy1.getEndDate());
        Assertions.assertEquals("POL1001", policy1.getPolicyNumber());

        verify(policyRepository).findById(1L);
        verify(policyRepository).save(policy1);
    }

    @Test
    public void updateInvalidId() {
        when(policyRepository.findById(10L)).thenReturn(Optional.empty());

        PolicyRequestDto dto = new PolicyRequestDto(
                LocalDate.of(2026, 5, 1), LocalDate.of(2027, 5, 1),
                PolicyStatus.ACTIVE, "POL2001");

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> policyService.update(10L, dto));

        Assertions.assertEquals("Policy Id invalid", exception.getMessage());
        verify(policyRepository).findById(10L);
        verify(policyRepository, never()).save(any());
    }

    @Test
    public void getAllByEmployeeTest() {
        when(policyRepository.findAllByProposalEmployeeUserUsername("employee@gmail.com"))
                .thenReturn(List.of(policy1, policy2));

        Assertions.assertEquals(2,
                policyService.getAllByEmployee("employee@gmail.com").size());

        verify(policyRepository).findAllByProposalEmployeeUserUsername("employee@gmail.com");
    }

    @Test
    public void getAllByEmployeeEmptyTest() {
        when(policyRepository.findAllByProposalEmployeeUserUsername("employee@gmail.com"))
                .thenReturn(List.of());

        Assertions.assertEquals(0,
                policyService.getAllByEmployee("employee@gmail.com").size());

        verify(policyRepository).findAllByProposalEmployeeUserUsername("employee@gmail.com");
    }
}

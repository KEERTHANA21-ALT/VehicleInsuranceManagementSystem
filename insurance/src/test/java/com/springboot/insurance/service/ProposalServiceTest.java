package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.ProposalRequestDto;
import com.springboot.insurance.dto.request.ProposalStatusRequestDto;
import com.springboot.insurance.dto.response.ProposalResponseDto;
import com.springboot.insurance.dto.response.ProposalResponseForAdminDto;
import com.springboot.insurance.dto.response.ProposalResponseForEmployeeDto;
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
import java.util.List;
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
        policyHolder1=new PolicyHolder();
        policyHolder1.setId(1L);

        vehicle1=new Vehicle();
        vehicle1.setId(1L);

        insurancePlan1=new InsurancePlan();
        insurancePlan1.setId(1L);
        insurancePlan1.setPlanType(PlanType.COMPREHENSIVE);
        insurancePlan1.setBasePremium(5000);
        insurancePlan1.setDiscountPercentage(10);

        employee1=new Employee();
        employee1.setId(1L);

        proposal1=new Proposal(1L,4500,5000,500,true,false,ProposalStatus.REJECTED,Instant.now(),policyHolder1,employee1,vehicle1,insurancePlan1);
        proposal2=new Proposal(2L,4500,5000,500,true,false,ProposalStatus.APPROVED,Instant.now(),policyHolder1,employee1,vehicle1,insurancePlan1);
    }

    @Test
    public void addTest() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(insurancePlanRepository.findById(1L)).thenReturn(Optional.of(insurancePlan1));
        when(proposalRepository.findByVehicleIdAndPolicyHolderIdAndProposalStatusNot(1L,1L,ProposalStatus.REJECTED)).thenReturn(Optional.empty());
        when(proposalRepository.save(any(Proposal.class))).thenReturn(proposal1);

        ProposalRequestDto dto=new ProposalRequestDto(1L,1L,List.of());

        proposalService.add("john@gmail.com",dto);

        ArgumentCaptor<Proposal> captor=ArgumentCaptor.forClass(Proposal.class);
        verify(proposalRepository).save(captor.capture());

        Assertions.assertEquals(5000,captor.getValue().getBasePremium());
        Assertions.assertEquals(500,captor.getValue().getDiscount());
        Assertions.assertEquals(4500,captor.getValue().getPremiumAmount());
        Assertions.assertEquals(policyHolder1,captor.getValue().getPolicyHolder());
        Assertions.assertEquals(vehicle1,captor.getValue().getVehicle());
        Assertions.assertEquals(insurancePlan1,captor.getValue().getInsurancePlan());
        Assertions.assertEquals(ProposalStatus.INSPECTION_PENDING,captor.getValue().getProposalStatus());
        Assertions.assertTrue(captor.getValue().isActive());
        Assertions.assertNull(captor.getValue().getEmployee());
    }

    @Test
    public void addPolicyHolderInvalid() {
        when(policyHolderRepository.findByUserUsername("wrong@gmail.com")).thenReturn(Optional.empty());

        ProposalRequestDto dto=new ProposalRequestDto(1L,1L,List.of());

        Assertions.assertEquals("Policy Holder not found",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.add("wrong@gmail.com",dto)).getMessage());

        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void addVehicleInvalid() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        ProposalRequestDto dto=new ProposalRequestDto(1L,1L,List.of());

        Assertions.assertEquals("Vehicle not found",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.add("john@gmail.com",dto)).getMessage());

        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void addInsurancePlanInvalid() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(insurancePlanRepository.findById(1L)).thenReturn(Optional.empty());

        ProposalRequestDto dto=new ProposalRequestDto(1L,1L,List.of());

        Assertions.assertEquals("Insurance Plan not found",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.add("john@gmail.com",dto)).getMessage());

        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void addExistingProposal() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(insurancePlanRepository.findById(1L)).thenReturn(Optional.of(insurancePlan1));
        when(proposalRepository.findByVehicleIdAndPolicyHolderIdAndProposalStatusNot(1L,1L,ProposalStatus.REJECTED)).thenReturn(Optional.of(proposal1));

        ProposalRequestDto dto=new ProposalRequestDto(1L,1L,List.of());

        Assertions.assertEquals("A proposal already exists for this vehicle",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.add("john@gmail.com",dto)).getMessage());

        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void getByIdPresent() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));

        Assertions.assertEquals(ProposalStatus.REJECTED,proposalService.getById(1L).proposalStatus());

        verify(proposalRepository).findById(1L);
    }

    @Test
    public void getByIdAbsent() {
        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Proposal Id is invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.getById(10L)).getMessage());

        verify(proposalRepository).findById(10L);
    }

    @Test
    public void getAllTest() {
        when(proposalRepository.findAllByPolicyHolderUserUsername("john@gmail.com")).thenReturn(List.of(proposal1,proposal2));

        List<ProposalResponseDto> result=proposalService.getAll("john@gmail.com");

        Assertions.assertEquals(2,result.size());

        verify(proposalRepository).findAllByPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getAllEmptyTest() {
        when(proposalRepository.findAllByPolicyHolderUserUsername("john@gmail.com")).thenReturn(List.of());

        List<ProposalResponseDto> result=proposalService.getAll("john@gmail.com");

        Assertions.assertEquals(0,result.size());

        verify(proposalRepository).findAllByPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void assignEmployeeTest() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        proposalService.assignEmployee(1L,1L);

        Assertions.assertEquals(employee1,proposal1.getEmployee());

        verify(proposalRepository).findById(1L);
        verify(employeeRepository).findById(1L);
        verify(proposalRepository).save(proposal1);
    }

    @Test
    public void assignEmployeeInvalidProposal() {
        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Proposal not found",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.assignEmployee(10L,1L)).getMessage());

        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void assignEmployeeInvalidEmployee() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));
        when(employeeRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Employee not found",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.assignEmployee(1L,10L)).getMessage());

        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void deleteTest() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));
        when(proposalRepository.save(proposal1)).thenReturn(proposal1);

        proposalService.delete(1L);

        Assertions.assertFalse(proposal1.isActive());

        verify(proposalRepository).findById(1L);
        verify(proposalRepository).save(proposal1);
    }

    @Test
    public void deleteInvalidId() {
        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Proposal Id invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.delete(10L)).getMessage());

        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void updateTest() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));

        ProposalStatusRequestDto dto=new ProposalStatusRequestDto(ProposalStatus.APPROVED);

        proposalService.update(1L,dto);

        Assertions.assertEquals(ProposalStatus.APPROVED,proposal1.getProposalStatus());

        verify(proposalRepository).findById(1L);
        verify(proposalRepository).save(proposal1);
    }

    @Test
    public void updateInvalidId() {
        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        ProposalStatusRequestDto dto=new ProposalStatusRequestDto(ProposalStatus.APPROVED);

        Assertions.assertEquals("Proposal Id invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.update(10L,dto)).getMessage());

        verify(proposalRepository).findById(10L);
        verify(proposalRepository,never()).save(any());
    }

    @Test
    public void getAllForAdminTest() {
        when(proposalRepository.findAll()).thenReturn(List.of(proposal1,proposal2));

        List<ProposalResponseForAdminDto> result=proposalService.getAllForAdmin();

        Assertions.assertEquals(2,result.size());

        verify(proposalRepository).findAll();
    }

    @Test
    public void getAllForAdminEmptyTest() {
        when(proposalRepository.findAll()).thenReturn(List.of());

        List<ProposalResponseForAdminDto> result=proposalService.getAllForAdmin();

        Assertions.assertEquals(0,result.size());

        verify(proposalRepository).findAll();
    }

    @Test
    public void getEmployeeProposalsTest() {
        when(employeeRepository.findByUserUsername("employee@gmail.com")).thenReturn(Optional.of(employee1));
        when(proposalRepository.findByEmployeeId(1L)).thenReturn(List.of(proposal1,proposal2));

        List<ProposalResponseForAdminDto> result=proposalService.getEmployeeProposals("employee@gmail.com");

        Assertions.assertEquals(2,result.size());

        verify(employeeRepository).findByUserUsername("employee@gmail.com");
        verify(proposalRepository).findByEmployeeId(1L);
    }

    @Test
    public void getEmployeeProposalsInvalidEmployee() {
        when(employeeRepository.findByUserUsername("wrong@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertEquals("Employee not found",Assertions.assertThrows(RuntimeException.class,()->proposalService.getEmployeeProposals("wrong@gmail.com")).getMessage());

        verify(proposalRepository,never()).findByEmployeeId(anyLong());
    }

    @Test
    public void getApprovedProposalsTest() {
        when(employeeRepository.findAllByUserUsername("employee@gmail.com")).thenReturn(employee1);
        when(proposalRepository.findByEmployeeAndProposalStatus(employee1,ProposalStatus.APPROVED)).thenReturn(List.of(proposal2));

        List<ProposalResponseForEmployeeDto> result=proposalService.getApprovedProposals("employee@gmail.com");

        Assertions.assertEquals(1,result.size());

        verify(employeeRepository).findAllByUserUsername("employee@gmail.com");
        verify(proposalRepository).findByEmployeeAndProposalStatus(employee1,ProposalStatus.APPROVED);
    }

    @Test
    public void getByIdForPolicyPresent() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));

        Assertions.assertEquals(ProposalStatus.REJECTED,proposalService.getByIdForPolicy(1L).proposalStatus());

        verify(proposalRepository).findById(1L);
    }

    @Test
    public void getByIdForPolicyAbsent() {
        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Proposal Id is invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->proposalService.getByIdForPolicy(10L)).getMessage());

        verify(proposalRepository).findById(10L);
    }
}


package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.dto.response.UploadResponseDto;
import com.springboot.insurance.enums.ClaimStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.Claim;
import com.springboot.insurance.model.Employee;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.model.Vehicle;
import com.springboot.insurance.repository.ClaimRepository;
import com.springboot.insurance.repository.EmployeeRepository;
import com.springboot.insurance.repository.PolicyRepository;
import com.springboot.insurance.utility.UploadUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClaimServiceTest {

    @InjectMocks
    private ClaimService claimService;
    @Mock
    private ClaimRepository claimRepository;
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UploadUtility uploadUtility;

    private Claim claim1;
    private Claim claim2;
    private Policy policy1;
    private Proposal proposal1;
    private Vehicle vehicle1;
    private Employee employee1;

    @BeforeEach
    public void init() {
        vehicle1 = new Vehicle();
        vehicle1.setId(1L);
        vehicle1.setVehicleNumber("TN10GH3456");
        vehicle1.setVehicleModel("Hyundai i20");

        proposal1 = new Proposal();
        proposal1.setId(1L);
        proposal1.setVehicle(vehicle1);

        policy1 = new Policy();
        policy1.setId(1L);
        policy1.setProposal(proposal1);

        employee1 = new Employee();
        employee1.setId(1L);

        claim1 = new Claim(1L, 50000, true, ClaimStatus.REJECTED, Instant.now(), "Accident", "Need more Actions", "claim1.jpg", policy1, employee1);
        claim2 = new Claim(2L, 25000, true, ClaimStatus.APPROVED, Instant.now(), "Flood Damage", "Approved", "claim2.jpg", policy1, employee1);
    }

    @Test
    public void addTest() {
        when(policyRepository.findByIdAndProposalPolicyHolderUserUsername(1L, "john@gmail.com")).thenReturn(Optional.of(policy1));
        when(claimRepository.save(any(Claim.class))).thenReturn(claim1);

        ClaimRequestDto dto = new ClaimRequestDto(1L, "Accident", "Need more Actions");
        claimService.add("john@gmail.com", dto);

        ArgumentCaptor<Claim> captor = ArgumentCaptor.forClass(Claim.class);
        verify(claimRepository, times(1)).save(captor.capture());

        Claim savedClaim = captor.getValue();
        Assertions.assertEquals(dto.claimReason(), savedClaim.getClaimReason());
        Assertions.assertEquals(dto.claimRemarks(), savedClaim.getClaimRemarks());
        Assertions.assertEquals(ClaimStatus.SUBMITTED, savedClaim.getClaimStatus());
        Assertions.assertEquals(policy1, savedClaim.getPolicy());
        Assertions.assertTrue(savedClaim.isActive());
    }

    @Test
    public void addInvalidPolicy() {
        when(policyRepository.findByIdAndProposalPolicyHolderUserUsername(10L, "john@gmail.com")).thenReturn(Optional.empty());

        ClaimRequestDto dto = new ClaimRequestDto(10L, "Accident", "Need more Actions");

        Assertions.assertEquals("Policy is invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.add("john@gmail.com", dto)).getMessage());
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void getMyClaimsTest() {
        when(claimRepository.findByPolicyProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(List.of(claim1, claim2));

        Assertions.assertEquals(2, claimService.getMyClaims("john@gmail.com").size());
        verify(claimRepository, times(1)).findByPolicyProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getMyClaimsEmptyTest() {
        when(claimRepository.findByPolicyProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(List.of());

        Assertions.assertEquals(0, claimService.getMyClaims("john@gmail.com").size());
        verify(claimRepository, times(1)).findByPolicyProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getByPolicyIdTest() {
        when(policyRepository.findByIdAndProposalPolicyHolderUserUsername(1L, "john@gmail.com")).thenReturn(Optional.of(policy1));
        when(claimRepository.findByPolicyId(1L)).thenReturn(List.of(claim1, claim2));

        Assertions.assertEquals(2, claimService.getByPolicyId("john@gmail.com", 1L).size());
        verify(policyRepository, times(1)).findByIdAndProposalPolicyHolderUserUsername(1L, "john@gmail.com");
        verify(claimRepository, times(1)).findByPolicyId(1L);
    }

    @Test
    public void getByPolicyIdInvalidPolicy() {
        when(policyRepository.findByIdAndProposalPolicyHolderUserUsername(10L, "john@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertEquals("Policy is invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.getByPolicyId("john@gmail.com", 10L)).getMessage());
        verify(claimRepository, never()).findByPolicyId(anyLong());
    }

    @Test
    public void getByIdPresent() {
        when(claimRepository.findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.of(claim1));

        Assertions.assertEquals(50000.0, claimService.getById("john@gmail.com").claimAmount());
        verify(claimRepository, times(1)).findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getByIdAbsent() {
        when(claimRepository.findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertEquals("Claim is Invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.getById("john@gmail.com")).getMessage());
        verify(claimRepository, times(1)).findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void deleteTest() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));
        when(claimRepository.save(claim1)).thenReturn(claim1);

        claimService.delete(1L);

        Assertions.assertFalse(claim1.isActive());
        verify(claimRepository, times(1)).findById(1L);
        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void deleteInvalidId() {
        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Claim Id invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.delete(10L)).getMessage());
        verify(claimRepository, times(1)).findById(10L);
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void updateTest() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));

        ClaimResponseDto dto = new ClaimResponseDto(1L, 50000.0, ClaimStatus.APPROVED, Instant.now(), "Accident", "Approved", 1L, "John Doe", "Employee", "claim1.jpg");
        claimService.update(1L, dto);

        Assertions.assertEquals(ClaimStatus.APPROVED, claim1.getClaimStatus());
        verify(claimRepository, times(1)).findById(1L);
        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void updateInvalidId() {
        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        ClaimResponseDto dto = new ClaimResponseDto(10L, 50000.0, ClaimStatus.APPROVED, Instant.now(), "Accident", "Approved", 1L, "John Doe", "Employee", "claim1.jpg");

        Assertions.assertEquals("Claim Id invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.update(10L, dto)).getMessage());
        verify(claimRepository, times(1)).findById(10L);
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void getSurveyorPendingTest() {
        when(claimRepository.findByClaimStatusIn(List.of(ClaimStatus.SUBMITTED, ClaimStatus.UNDER_REVIEW))).thenReturn(List.of(claim1, claim2));

        Assertions.assertEquals(2, claimService.getSurveyorPending().size());
        verify(claimRepository, times(1)).findByClaimStatusIn(List.of(ClaimStatus.SUBMITTED, ClaimStatus.UNDER_REVIEW));
    }

    @Test
    public void getSurveyorPendingEmptyTest() {
        when(claimRepository.findByClaimStatusIn(List.of(ClaimStatus.SUBMITTED, ClaimStatus.UNDER_REVIEW))).thenReturn(List.of());

        Assertions.assertEquals(0, claimService.getSurveyorPending().size());
        verify(claimRepository, times(1)).findByClaimStatusIn(List.of(ClaimStatus.SUBMITTED, ClaimStatus.UNDER_REVIEW));
    }

    @Test
    public void surveyorReviewTest() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));

        ClaimResponseDto dto = new ClaimResponseDto(1L, 60000.0, ClaimStatus.UNDER_REVIEW, Instant.now(), "Updated Accident", "Need Review", 1L, "John Doe", "Employee", "claim1.jpg");
        claimService.surveyorReview(1L, dto);

        Assertions.assertEquals(60000.0, claim1.getClaimAmount());
        Assertions.assertEquals("Updated Accident", claim1.getClaimReason());
        Assertions.assertEquals("Need Review", claim1.getClaimRemarks());
        Assertions.assertEquals(ClaimStatus.UNDER_REVIEW, claim1.getClaimStatus());
        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void surveyorReviewInvalidId() {
        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        ClaimResponseDto dto = new ClaimResponseDto(10L, 60000.0, ClaimStatus.UNDER_REVIEW, Instant.now(), "Accident", "Review", 1L, "John Doe", "Employee", "claim1.jpg");

        Assertions.assertEquals("Claim Id invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.surveyorReview(10L, dto)).getMessage());
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void getManagerPendingTest() {
        when(claimRepository.findByClaimStatus(ClaimStatus.UNDER_REVIEW)).thenReturn(List.of(claim1, claim2));

        Assertions.assertEquals(2, claimService.getManagerPending().size());
        verify(claimRepository, times(1)).findByClaimStatus(ClaimStatus.UNDER_REVIEW);
    }

    @Test
    public void getManagerPendingEmptyTest() {
        when(claimRepository.findByClaimStatus(ClaimStatus.UNDER_REVIEW)).thenReturn(List.of());

        Assertions.assertEquals(0, claimService.getManagerPending().size());
        verify(claimRepository, times(1)).findByClaimStatus(ClaimStatus.UNDER_REVIEW);
    }

    @Test
    public void managerDecisionTest() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));

        ClaimResponseDto dto = new ClaimResponseDto(1L, 50000.0, ClaimStatus.APPROVED, Instant.now(), "Accident", "Approved", 1L, "John Doe", "Employee", "claim1.jpg");
        claimService.managerDecision(1L, dto);

        Assertions.assertEquals(ClaimStatus.APPROVED, claim1.getClaimStatus());
        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void managerDecisionInvalidId() {
        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        ClaimResponseDto dto = new ClaimResponseDto(10L, 50000.0, ClaimStatus.APPROVED, Instant.now(), "Accident", "Approved", 1L, "John Doe", "Employee", "claim1.jpg");

        Assertions.assertEquals("Claim Id invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.managerDecision(10L, dto)).getMessage());
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void getInsuranceManagerPaymentTest() {
        when(claimRepository.findByClaimStatus(ClaimStatus.APPROVED)).thenReturn(List.of(claim2));

        Assertions.assertEquals(1, claimService.getInsuranceManagerPayment().size());
        verify(claimRepository, times(1)).findByClaimStatus(ClaimStatus.APPROVED);
    }

    @Test
    public void getInsuranceManagerPaymentEmptyTest() {
        when(claimRepository.findByClaimStatus(ClaimStatus.APPROVED)).thenReturn(List.of());

        Assertions.assertEquals(0, claimService.getInsuranceManagerPayment().size());
        verify(claimRepository, times(1)).findByClaimStatus(ClaimStatus.APPROVED);
    }

    @Test
    public void payClaimTest() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));

        claimService.payClaim(1L);

        Assertions.assertEquals(ClaimStatus.PAID, claim1.getClaimStatus());
        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void payClaimInvalidId() {
        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Claim Id invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.payClaim(10L)).getMessage());
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void assignSurveyorTest() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        claimService.assignSurveyor(1L, 1L);

        Assertions.assertEquals(employee1, claim1.getEmployee());
        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void assignSurveyorInvalidClaim() {
        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Claim Id Invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.assignSurveyor(10L, 1L)).getMessage());
        verify(employeeRepository, never()).findById(anyLong());
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void assignSurveyorInvalidEmployee() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));
        when(employeeRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Employee Id Invalid", Assertions.assertThrows(ResourceNotFoundException.class, () -> claimService.assignSurveyor(1L, 10L)).getMessage());
        verify(claimRepository, never()).save(any());
    }

    @Test
    public void getAllTest() {
        when(claimRepository.findAll()).thenReturn(List.of(claim1, claim2));

        Assertions.assertEquals(2, claimService.getAll().size());
        verify(claimRepository, times(1)).findAll();
    }

    @Test
    public void getAllEmptyTest() {
        when(claimRepository.findAll()).thenReturn(List.of());

        Assertions.assertEquals(0, claimService.getAll().size());
        verify(claimRepository, times(1)).findAll();
    }


    @Test
    public void uploadImageTest() throws Exception {

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "claim.jpg",
                "image/jpeg",
                "test image".getBytes()
        );

        when(claimRepository.save(any(Claim.class))).thenReturn(claim1);

        UploadResponseDto response = claimService.uploadImage(1L, imageFile);

        Assertions.assertEquals(1L, response.id());
        Assertions.assertEquals("claim.jpg", response.fileName());
        Assertions.assertEquals("File upload success", response.message());

        Assertions.assertNotNull(claim1.getImageUrl());

        verify(claimRepository, times(1)).findById(1L);
        verify(uploadUtility, times(1)).validateImage(imageFile);
        verify(claimRepository, times(1)).save(claim1);
    }


    @Test
    public void uploadImageInvalidClaim() throws Exception {

        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Claim Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> claimService.uploadImage(10L, null)
                ).getMessage()
        );

        verify(claimRepository, times(1)).findById(10L);
        verify(uploadUtility, never()).validateImage(any());
    }



}

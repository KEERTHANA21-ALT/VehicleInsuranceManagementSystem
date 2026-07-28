package com.springboot.insurance.service;


import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.enums.ClaimStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.Claim;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.repository.ClaimRepository;
import com.springboot.insurance.repository.PolicyRepository;
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
public class ClaimServiceTest {

    @InjectMocks
    private ClaimService claimService;

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private PolicyRepository policyRepository;

    private Claim claim1;
    private Claim claim2;

    private Policy policy1;

    @BeforeEach
    public void init() {

        policy1 = new Policy();
        policy1.setId(1L);


        claim1 = new Claim(
                1L,
                50000,
                true,
                ClaimStatus.REJECTED,
                Instant.now(),
                "Accident",
                "Need more Actions",
                policy1
        );

        claim2 = new Claim(
                2L,
                25000,
                true,
                ClaimStatus.APPROVED,
                Instant.now(),
                "Flood Damage",
                "Approved",
                policy1
        );
    }

    @Test
    public void addTest() {

        when(policyRepository.findByProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.of(policy1));

        ClaimRequestDto dto = new ClaimRequestDto(
                50000,
                ClaimStatus.REJECTED,
                "Accident",
                "Need more Actions"
        );

        claimService.add("john@gmail.com", dto);

        ArgumentCaptor<Claim> captor = ArgumentCaptor.forClass(Claim.class);

        verify(claimRepository, times(1)).save(captor.capture());

        Assertions.assertEquals(dto.claimAmount(), captor.getValue().getClaimAmount());
        Assertions.assertEquals(dto.claimReason(), captor.getValue().getClaimReason());
        Assertions.assertEquals(dto.claimRemarks(), captor.getValue().getClaimRemarks());
        Assertions.assertEquals(policy1, captor.getValue().getPolicy());
        Assertions.assertTrue(captor.getValue().isActive());
    }

    @Test
    public void getByIdPresent() {

        when(claimRepository.findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.of(claim1));

        Assertions.assertEquals(
                50000,
                claimService.getById("john@gmail.com").claimAmount()
        );

        verify(claimRepository, times(1)).findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getByIdAbsent() {

        when(claimRepository.findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Claim is Invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> claimService.getById("john@gmail.com")
                ).getMessage()
        );

        verify(claimRepository, times(1)).findFirstByPolicyProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getByPolicyIdTest() {

        List<Claim> list = List.of(claim1, claim2);

        when(claimRepository.findByPolicyProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(list);

        Assertions.assertEquals(
                2,
                claimService.getByPolicyId("john@gmail.com").size()
        );

        verify(claimRepository, times(1)).findByPolicyProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void deleteTest() {

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim1));

        when(claimRepository.save(claim1)).thenReturn(claim1);

        claimService.delete(1L);

        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void deleteInvalidId() {

        when(claimRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Claim Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> claimService.delete(10L)
                ).getMessage()
        );

        verify(claimRepository, never()).save(any());
    }

    @Test
    public void updateTest() {

        when(claimRepository.findById(1L))
                .thenReturn(Optional.of(claim1));

        ClaimResponseDto dto = new ClaimResponseDto(
                1L,
                ClaimStatus.REJECTED,
                Instant.now(),
                "Accident",
                "Need More Actions"
        );

        claimService.update(1L, dto);

        verify(claimRepository, times(1)).findById(1L);
        verify(claimRepository, times(1)).save(claim1);
    }

    @Test
    public void updateInvalidId() {

        when(claimRepository.findById(10L))
                .thenReturn(Optional.empty());

        ClaimResponseDto dto = new ClaimResponseDto(
                1L,
                ClaimStatus.APPROVED,
                Instant.now(),
                "Accident",
                "Need more Actions"
        );

        Assertions.assertEquals(
                "Claim Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> claimService.update(10L, dto)
                ).getMessage()
        );

        verify(claimRepository, times(1)).findById(10L);
        verify(claimRepository, times(0)).save(claim1);
    }

}

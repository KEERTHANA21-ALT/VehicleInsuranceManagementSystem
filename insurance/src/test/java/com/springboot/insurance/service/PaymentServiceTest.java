package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.PaymentRequestDto;
import com.springboot.insurance.enums.PaymentMethod;
import com.springboot.insurance.enums.PaymentStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.Payment;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.repository.PaymentRepository;
import com.springboot.insurance.repository.ProposalRepository;
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
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ProposalRepository proposalRepository;

    private Proposal proposal1;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    public void init() {
        proposal1=new Proposal();
        proposal1.setId(1L);
        proposal1.setPremiumAmount(4500);

        payment1=new Payment(1L,4500,true,PaymentMethod.UPI,Instant.now(),PaymentStatus.PENDING,proposal1);
        payment2=new Payment(2L,6000,true,PaymentMethod.CREDIT_CARD,Instant.now(),PaymentStatus.SUCCESS,proposal1);
    }

    @Test
    public void addTest() {
        when(proposalRepository.findById(1L)).thenReturn(Optional.of(proposal1));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment1);

        PaymentRequestDto dto=new PaymentRequestDto(PaymentMethod.UPI);

        paymentService.add(1L,dto);

        ArgumentCaptor<Payment> captor=ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository,times(1)).save(captor.capture());

        Assertions.assertEquals(dto.paymentMethod(),captor.getValue().getPaymentMethod());
        Assertions.assertEquals(4500,captor.getValue().getAmount());
        Assertions.assertEquals(PaymentStatus.PENDING,captor.getValue().getPaymentStatus());
        Assertions.assertEquals(proposal1,captor.getValue().getProposal());
        Assertions.assertTrue(captor.getValue().isActive());
    }

    @Test
    public void addInvalidProposal() {
        when(proposalRepository.findById(10L)).thenReturn(Optional.empty());

        PaymentRequestDto dto=new PaymentRequestDto(PaymentMethod.UPI);

        Assertions.assertEquals("Proposal is invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->paymentService.add(10L,dto)).getMessage());

        verify(paymentRepository,never()).save(any());
    }

    @Test
    public void getByIdPresent() {
        when(paymentRepository.findByProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.of(payment1));

        Assertions.assertEquals(PaymentMethod.UPI,paymentService.getById("john@gmail.com").paymentMethod());

        verify(paymentRepository,times(1)).findByProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getByIdAbsent() {
        when(paymentRepository.findByProposalPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertEquals("Payment id invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->paymentService.getById("john@gmail.com")).getMessage());

        verify(paymentRepository,times(1)).findByProposalPolicyHolderUserUsername("john@gmail.com");
    }

    @Test
    public void getByProposalIdTest() {
        List<Payment> list=List.of(payment1,payment2);

        when(paymentRepository.findByProposalId(1L)).thenReturn(list);

        Assertions.assertEquals(2,paymentService.getByProposalId(1L).size());

        verify(paymentRepository,times(1)).findByProposalId(1L);
    }

    @Test
    public void getByProposalIdEmptyTest() {
        when(paymentRepository.findByProposalId(10L)).thenReturn(List.of());

        Assertions.assertEquals(0,paymentService.getByProposalId(10L).size());

        verify(paymentRepository,times(1)).findByProposalId(10L);
    }

    @Test
    public void deleteTest() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment1));
        when(paymentRepository.save(payment1)).thenReturn(payment1);

        paymentService.delete(1L);

        Assertions.assertFalse(payment1.isActive());
        verify(paymentRepository,times(1)).findById(1L);
        verify(paymentRepository,times(1)).save(payment1);
    }

    @Test
    public void deleteInvalidId() {
        when(paymentRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Payment Id invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->paymentService.delete(10L)).getMessage());

        verify(paymentRepository,never()).save(any());
    }

    @Test
    public void updateTest() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment1));
        when(paymentRepository.save(payment1)).thenReturn(payment1);

        paymentService.update(1L,PaymentStatus.SUCCESS);

        Assertions.assertEquals(PaymentStatus.SUCCESS,payment1.getPaymentStatus());
        verify(paymentRepository,times(1)).findById(1L);
        verify(paymentRepository,times(1)).save(payment1);
    }

    @Test
    public void updateInvalidId() {
        when(paymentRepository.findById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Payment Id invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->paymentService.update(10L,PaymentStatus.SUCCESS)).getMessage());

        verify(paymentRepository,times(1)).findById(10L);
        verify(paymentRepository,never()).save(any());
    }
}


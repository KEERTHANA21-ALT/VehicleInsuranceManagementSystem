package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.PaymentRequestDto;
import com.springboot.insurance.dto.response.PaymentResponseDto;
import com.springboot.insurance.enums.PaymentStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.PaymentMapper;
import com.springboot.insurance.mapper.ProposalMapper;
import com.springboot.insurance.model.Payment;
import com.springboot.insurance.model.Proposal;
import com.springboot.insurance.repository.PaymentRepository;
import com.springboot.insurance.repository.ProposalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProposalRepository proposalRepository;


    public void add(long  proposalId, PaymentRequestDto dto) {

        // Step 1: Fetch existing proposal
        Proposal proposal = proposalRepository.findById(proposalId)
                        .orElseThrow(() -> new ResourceNotFoundException("Proposal Id invalid"));

        // Step 2: convert dto to Payment entity
        Payment payment = PaymentMapper.convertDtoToEntity(dto);

        // Step 3: Attach proposal details
        payment.setAmount(proposal.getPremiumAmount());

        // Step 4: Set payment amount from proposal
        payment.setPaymentStatus(PaymentStatus.PENDING);

        // Step 5: Set payment status
        payment.setProposal(proposal);

        // Step 6: Save payment
        paymentRepository.save(payment);
    }

    public PaymentResponseDto getById(long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Payment id invalid"));

        return PaymentMapper.convertEntityToDto(payment);
    }

    public List<PaymentResponseDto> getByProposalId(long proposalId) {
        List<Payment> list = paymentRepository.findByProposalId(proposalId);

        return list
                .stream()
                .map(PaymentMapper :: convertEntityToDto)
                .toList();
    }
}

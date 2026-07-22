package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.PaymentRequestDto;
import com.springboot.insurance.dto.response.PaymentResponseDto;
import com.springboot.insurance.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/add/{proposalId}")
    public void add(@PathVariable long proposalId, @Valid @RequestBody PaymentRequestDto dto){
        paymentService.add( proposalId,dto);
    }

    @GetMapping("/get-one/{id}")
    public PaymentResponseDto getById(@PathVariable long id){
        return paymentService.getById(id);
    }

    @GetMapping("/get-ByProposalId/{proposalId}")
    public List<PaymentResponseDto> getByProposalId(@PathVariable long proposalId){
        return paymentService.getByProposalId(proposalId);
    }



}

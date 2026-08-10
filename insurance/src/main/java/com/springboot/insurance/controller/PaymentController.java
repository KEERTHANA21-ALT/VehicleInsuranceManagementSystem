package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.PaymentRequestDto;
import com.springboot.insurance.dto.response.PaymentResponseDto;
import com.springboot.insurance.enums.PaymentStatus;
import com.springboot.insurance.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/add/{proposalId}")
    public PaymentResponseDto add(@PathVariable long proposalId, @Valid @RequestBody PaymentRequestDto dto){
        return paymentService.add(proposalId,dto);
    }

    @GetMapping("/get-one")
    public PaymentResponseDto getById(Principal principal){
        String username = principal.getName();
        return paymentService.getById(username);
    }

    @GetMapping("/get-ByProposalId/{proposalId}")
    public List<PaymentResponseDto> getByProposalId(@PathVariable long proposalId){
        return paymentService.getByProposalId(proposalId);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        paymentService.delete(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable long id, @Valid @RequestParam PaymentStatus paymentStatus){
        paymentService.update(id,paymentStatus);
    }


}

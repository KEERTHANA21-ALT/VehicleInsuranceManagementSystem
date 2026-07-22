package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.PolicyRequestDto;
import com.springboot.insurance.dto.response.PolicyResponseDto;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping("/add/{proposalId}")
    public void add(@PathVariable long proposalId, @Valid @RequestBody PolicyRequestDto dto){
        policyService.add(proposalId,dto);
    }

    @GetMapping("/get-one")
    public PolicyResponseDto getById(Principal principal){
        String username = principal.getName();
        return policyService.getById(username);
    }

    @GetMapping("/get-myPolicies")
    public List<PolicyResponseDto> getAll(Principal principal){
        String username = principal.getName();
        return policyService.getAll(username);
    }
}

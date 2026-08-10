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
@CrossOrigin(origins = "http://localhost:5173")
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping("/add/{proposalId}")
    public void add(@PathVariable long proposalId, @Valid @RequestBody PolicyRequestDto dto){
        policyService.add(proposalId,dto);
    }

    @GetMapping("/get-one/{id}")
    public PolicyResponseDto getById(@PathVariable long id, Principal principal){
        String username = principal.getName();
        return policyService.getById(id,username);
    }

    @GetMapping("/get-myPolicies")
    public List<PolicyResponseDto> getAll(Principal principal){
        String username = principal.getName();
        return policyService.getAll(username);
    }
    @GetMapping("/employee/get-all")
    public List<PolicyResponseDto> getAllByEmployee(Principal principal){
        String username = principal.getName();
        return policyService.getAllByEmployee(username);
    }


    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        policyService.delete(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable long id,@Valid @RequestBody PolicyRequestDto dto){
        policyService.update(id,dto);
    }
}

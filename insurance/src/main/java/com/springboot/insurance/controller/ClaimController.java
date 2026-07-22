package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claim")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/add/{policyId}")
    public void add(@PathVariable long policyId,
                    @RequestBody ClaimRequestDto dto){
        claimService.add(policyId,dto);

    }

    @GetMapping("/get-one/{id}")
    public ClaimResponseDto getById(@PathVariable long id){
        return claimService.getById(id);
    }

    @GetMapping("/get-ByPolicyId/{policyId}")
    public List<ClaimResponseDto> getByPolicyId(@PathVariable long policyId){
        return claimService.getByPolicyId(policyId);
    }

}

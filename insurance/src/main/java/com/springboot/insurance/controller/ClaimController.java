package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/claim")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/add")
    public void add(Principal principal, @RequestBody ClaimRequestDto dto){
        String username = principal.getName();
        claimService.add(username,dto);

    }

    @GetMapping("/get-one")
    public ClaimResponseDto getById(Principal principal){
        String username = principal.getName();
        return claimService.getById(username);
    }

    @GetMapping("/get-ByPolicyId")
    public List<ClaimResponseDto> getByPolicyId(Principal principal){
        String username = principal.getName();
        return claimService.getByPolicyId(username);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        claimService.delete(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable long id, @Valid @RequestBody ClaimResponseDto dto){
        claimService.update(id,dto);
    }

}

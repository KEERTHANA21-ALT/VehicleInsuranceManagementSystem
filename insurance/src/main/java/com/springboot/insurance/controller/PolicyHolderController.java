package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.PolicyHolderRequestDto;
import com.springboot.insurance.dto.response.PolicyHolderResponseDto;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.service.PolicyHolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/policyHolder")
@RequiredArgsConstructor
public class PolicyHolderController {

    private final PolicyHolderService policyHolderService;

    @PostMapping("/add")
    public void add(@Valid @RequestBody PolicyHolderRequestDto dto){
        policyHolderService.add(dto);
    }

    @GetMapping("/get-all")
    public List<PolicyHolderResponseDto> getAll(@RequestParam Integer page,@RequestParam Integer size){
        return policyHolderService.getAll(page,size);
    }

    @GetMapping("/get-one/{id}")
    public PolicyHolderResponseDto getById(@PathVariable long id){
        return policyHolderService.getById(id);

    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        policyHolderService.delete(id);

    }

    @PutMapping("/update")
    public void update(Principal principal, @Valid @RequestBody PolicyHolderRequestDto policyHolderRequestDto){
        String username = principal.getName();
        policyHolderService.update(username, policyHolderRequestDto);

    }



}

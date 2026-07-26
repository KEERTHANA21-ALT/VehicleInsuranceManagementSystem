package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.ProposalRequestDto;
import com.springboot.insurance.dto.response.ProposalResponseDto;
import com.springboot.insurance.enums.ProposalStatus;
import com.springboot.insurance.service.ProposalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/proposal")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping("/add")
    public void add(@Valid @RequestBody ProposalRequestDto dto, Principal principal) {

        String username = principal.getName();
        proposalService.add(username,dto);
    }

    @GetMapping("/get-one/{proposalId}")
    public ProposalResponseDto getById(@PathVariable long proposalId){
        return proposalService.getById(proposalId);
    }

    @GetMapping("/get-myProposals")
    public List<ProposalResponseDto> getAll(Principal principal){
        String username = principal.getName();
        return proposalService.getAll(username);
    }


    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        proposalService.delete(id);
    }

    // the employee value while adding proposal is set to null becoz employee will be added by admin
    // once the proposal is created
    @PutMapping("/assign-employee/{proposalId}/{employeeId}")
    public void assignEmployee(@PathVariable long proposalId, @PathVariable long employeeId) {
        proposalService.assignEmployee(proposalId, employeeId);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable long id, @Valid @RequestBody ProposalRequestDto dto){
        proposalService.update(id,dto);
    }
}

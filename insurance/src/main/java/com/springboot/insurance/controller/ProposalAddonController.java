package com.springboot.insurance.controller;


import com.springboot.insurance.dto.response.ProposalAddonResponseDto;
import com.springboot.insurance.service.ProposalAddonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/proposal-addon")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProposalAddonController {

    private final ProposalAddonService proposalAddonService;


    @PostMapping("/add/{proposalId}/{addonId}")
    public void add(@PathVariable Long proposalId,
                    @PathVariable Long addonId,
                    Principal principal) {

        String username = principal.getName();
        proposalAddonService.add(proposalId, addonId, username);
    }

    @GetMapping("/get-proposalId/{proposalId}/addonId/{addonId}")
    public ProposalAddonResponseDto getById(@PathVariable Long proposalId,
                                            @PathVariable Long addonId,
                                            Principal principal){
        String username = principal.getName();
        return proposalAddonService.getById(proposalId, addonId, username);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        proposalAddonService.delete(id);
    }
}

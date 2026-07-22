package com.springboot.insurance.controller;


import com.springboot.insurance.dto.response.ProposalAddonResponseDto;
import com.springboot.insurance.service.ProposalAddonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proposal-addon")
@RequiredArgsConstructor
public class ProposalAddonController {

    private final ProposalAddonService proposalAddonService;


    @PostMapping("/add/{proposalId}/{addonId}")
    public void add(@PathVariable Long proposalId, @PathVariable Long addonId){
        proposalAddonService.add(proposalId, addonId);
    }

    @GetMapping("/get-proposalId/{proposalId}/addonId/{addonId}")
    public ProposalAddonResponseDto getById(@PathVariable Long proposalId,
                                                    @PathVariable Long addonId){
        return proposalAddonService.getById(proposalId,addonId);
    }
}

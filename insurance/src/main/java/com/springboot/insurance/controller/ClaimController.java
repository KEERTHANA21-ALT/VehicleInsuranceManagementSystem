package com.springboot.insurance.controller;

import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.dto.response.UploadResponseDto;
import com.springboot.insurance.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/claim")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClaimController {

    private final ClaimService claimService;


    @PostMapping("/add")
    public ClaimResponseDto add(Principal principal, @Valid @RequestBody ClaimRequestDto dto) {
        String username = principal.getName();
        return claimService.add(username, dto);
    }


    @GetMapping("/get-my-claims")
    public List<ClaimResponseDto> getMyClaims(Principal principal) {
        String username = principal.getName();
        return claimService.getMyClaims(username);
    }


    @GetMapping("/get-by-policy/{policyId}")
    public List<ClaimResponseDto> getByPolicyId(Principal principal, @PathVariable long policyId) {
        String username = principal.getName();
        return claimService.getByPolicyId(username, policyId);
    }

    @GetMapping("/get-one")
    public ClaimResponseDto getById(Principal principal) {
        String username = principal.getName();
        return claimService.getById(username);
    }


    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id) {
        claimService.delete(id);
    }


    @PutMapping("/update/{id}")
    public void update(@PathVariable long id, @Valid @RequestBody ClaimResponseDto dto) {
        claimService.update(id, dto);
    }


    // Claims waiting for surveyor
    @GetMapping("/surveyor/pending")
    public List<ClaimResponseDto> getSurveyorPending() {
        return claimService.getSurveyorPending();
    }


    // Surveyor reviews claim
    @PutMapping("/surveyor/review/{id}")
    public void surveyorReview(@PathVariable long id, @RequestBody ClaimResponseDto dto) {
        claimService.surveyorReview(id, dto);
    }


    // Claims waiting for claim manager
    @GetMapping("/manager/pending")
    public List<ClaimResponseDto> getManagerPending() {
        return claimService.getManagerPending();
    }

    @PutMapping("/manager/decision/{id}")
    public void managerDecision(@PathVariable long id, @RequestBody ClaimResponseDto dto) {
        claimService.managerDecision(id, dto);
    }


    // Approved claims waiting for payment
    @GetMapping("/insurance-manager/payment")
    public List<ClaimResponseDto> getPaymentClaims() {
        return claimService.getInsuranceManagerPayment();
    }


    // Pay claim
    @PutMapping("/insurance-manager/payment/{id}")
    public void payClaim(@PathVariable long id) {
        claimService.payClaim(id);
    }

    @PostMapping("/image/upload/{claimId}")
    public UploadResponseDto uploadImage(@PathVariable long claimId,
                                         @RequestParam("cImage") MultipartFile imageFile) throws IOException, InterruptedException{
        return claimService.uploadImage(claimId,imageFile);
    }

    @PutMapping("/assign-surveyor/{claimId}/{employeeId}")
    public void assignSurveyor(@PathVariable long claimId, @PathVariable long employeeId) {
        claimService.assignSurveyor(claimId, employeeId);
    }

    @GetMapping("/get-all")
    public List<ClaimResponseDto> getAll(){
        return claimService.getAll();
    }
}
package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.ClaimRequestDto;
import com.springboot.insurance.dto.response.ClaimResponseDto;
import com.springboot.insurance.dto.response.UploadResponseDto;
import com.springboot.insurance.enums.ClaimStatus;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.ClaimMapper;
import com.springboot.insurance.model.Claim;
import com.springboot.insurance.model.Employee;
import com.springboot.insurance.model.Policy;
import com.springboot.insurance.repository.ClaimRepository;
import com.springboot.insurance.repository.EmployeeRepository;
import com.springboot.insurance.repository.PolicyRepository;
import com.springboot.insurance.utility.UploadUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final EmployeeRepository employeeRepository;

    private final UploadUtility uploadUtility;

    private static final String claimUploadPath = "C:/Users/ad/Desktop/insurance-app/public/images/claims";



    public ClaimResponseDto add(String username, ClaimRequestDto dto) {
        Policy policy = policyRepository.findByIdAndProposalPolicyHolderUserUsername(dto.policyId(),username)
                .orElseThrow(() -> new ResourceNotFoundException("Policy is invalid"));

        // Convert DTO to Claim
        Claim claim = ClaimMapper.convertDtoToEntity(dto);

        claim.setActive(true);

        // New claim starts as PENDING
        claim.setClaimStatus(ClaimStatus.SUBMITTED);

        // Attach policy
        claim.setPolicy(policy);

        // Save claim
        Claim savedClaim = claimRepository.save(claim);

        return ClaimMapper.convertEntityToDto(savedClaim);
    }


    public List<ClaimResponseDto> getMyClaims(String username) {

        List<Claim> claims = claimRepository.findByPolicyProposalPolicyHolderUserUsername(username);

        return claims
                .stream()
                .map(ClaimMapper::convertEntityToDto)
                .toList();
    }



    public List<ClaimResponseDto> getByPolicyId(String username, long policyId) {

        policyRepository.findByIdAndProposalPolicyHolderUserUsername(policyId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Policy is invalid"));

        List<Claim> claims = claimRepository.findByPolicyId(policyId);

        return claims
                .stream()
                .map(ClaimMapper::convertEntityToDto)
                .toList();
    }



    public ClaimResponseDto getById(String username) {

        Claim claim = claimRepository.findFirstByPolicyProposalPolicyHolderUserUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("Claim is Invalid"));

        return ClaimMapper.convertEntityToDto(claim);
    }



    public void delete(long id) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim Id invalid"));

        claim.setActive(false);
        claimRepository.save(claim);
    }


    public void update(long id, @Valid ClaimResponseDto dto) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim Id invalid"));

        claim.setClaimStatus(dto.claimStatus());
        claimRepository.save(claim);
    }


    public List<ClaimResponseDto> getSurveyorPending() {

        List<Claim> claims = claimRepository.findByClaimStatusIn(
                List.of(
                        ClaimStatus.SUBMITTED,
                        ClaimStatus.UNDER_REVIEW
                )
        );

        return claims
                .stream()
                .map(ClaimMapper::convertEntityToDto)
                .toList();
    }



    public void surveyorReview(long id, ClaimResponseDto dto) {

        Claim claim = claimRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Claim Id invalid"));


        claim.setClaimAmount(dto.claimAmount());
        claim.setClaimReason(dto.claimReason());
        claim.setClaimRemarks(dto.claimRemarks());


        claim.setClaimStatus(ClaimStatus.UNDER_REVIEW);
        claimRepository.save(claim);
    }


    public List<ClaimResponseDto> getManagerPending() {

        List<Claim> claims = claimRepository.findByClaimStatus(ClaimStatus.UNDER_REVIEW);

        return claims
                .stream()
                .map(ClaimMapper::convertEntityToDto)
                .toList();
    }



    public void managerDecision(long id, ClaimResponseDto dto) {

        Claim claim = claimRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Claim Id invalid"));

        claim.setClaimStatus(dto.claimStatus());
        claimRepository.save(claim);
    }



    // Get approved claims waiting for payment
    public List<ClaimResponseDto> getInsuranceManagerPayment() {

        List<Claim> claims = claimRepository.findByClaimStatus(ClaimStatus.APPROVED);

        return claims
                .stream()
                .map(ClaimMapper::convertEntityToDto)
                .toList();
    }


    // Pay claim
    public void payClaim(long id) {

        Claim claim = claimRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                        "Claim Id invalid"));

        claim.setClaimStatus(ClaimStatus.PAID);

        claimRepository.save(claim);
    }

    public UploadResponseDto uploadImage(long claimId, MultipartFile imageFile) throws IOException {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim Id invalid"));

        uploadUtility.validateImage(imageFile);

        Path uploadDirectory = Paths.get(claimUploadPath);

        Files.createDirectories(uploadDirectory);

       // Path filePath = uploadDirectory.resolve(Objects.requireNonNull(imageFile.getOriginalFilename()));

        String originalFileName = Objects.requireNonNull(imageFile.getOriginalFilename());

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString() + extension;

        Path filePath = uploadDirectory.resolve(fileName);

        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        claim.setImageUrl(filePath.toString());

        claim = claimRepository.save(claim);

        return new UploadResponseDto(
                claim.getId(),
                claim.getImageUrl(),
                imageFile.getOriginalFilename(),
                "File upload success"
        );
    }

    public void assignSurveyor(long claimId, long employeeId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim Id Invalid"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Id Invalid"));

        claim.setEmployee(employee);

        claimRepository.save(claim);


    }

    public List<ClaimResponseDto> getAll() {
        List<Claim> list = claimRepository.findAll();

        return list
                .stream()
                .map(ClaimMapper :: convertEntityToDto)
                .toList();
    }
}
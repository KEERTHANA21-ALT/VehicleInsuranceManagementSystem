package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.VehicleRequestDto;
import com.springboot.insurance.dto.response.UploadResponseDto;
import com.springboot.insurance.dto.response.VehicleResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.VehicleMapper;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.Vehicle;
import com.springboot.insurance.repository.PolicyHolderRepository;
import com.springboot.insurance.repository.VehicleRepository;
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

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PolicyHolderRepository policyHolderRepository;

    private final UploadUtility uploadUtility;
    private static final String uploadPath = "C:/Users/ad/Desktop/insurance-app/public/images";


    public VehicleResponseDto  add(String userName, VehicleRequestDto dto) {

        // Step 1: Fetch PolicyHolder from given policyHolderId
        PolicyHolder policyHolder = policyHolderRepository.findByUserUsername(userName)
                .orElseThrow(()->new ResourceNotFoundException("PolicyHolder Id invalid"));

        // Step 2: Convert dto to Entity
        Vehicle vehicle = VehicleMapper.convertDtoToEntity(dto);

        // Step 3: Attach policyHolder to vehicle
        vehicle.setPolicyHolder(policyHolder);

        vehicle.setActive(true);

        // Step 4: Save vehicle in Db
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return VehicleMapper.convertEntityToDto(savedVehicle);
    }

    public VehicleResponseDto getByVehicleNumber(String vehicleNumber) {

        Vehicle vehicle = vehicleRepository.getByVehicleNumber(vehicleNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Vehicle Number invalid"));

        return VehicleMapper.convertEntityToDto(vehicle);
    }

    public List<VehicleResponseDto> getByPolicyHolder(long policyHolderId) {
        List<Vehicle> list = vehicleRepository.findByPolicyHolderId(policyHolderId);

        return list
                .stream()
                .map(VehicleMapper :: convertEntityToDto)
                .toList();
    }

    public void delete(long id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Vehicle Id invalid"));

        vehicle.setActive(false);

        vehicleRepository.save(vehicle);
    }

    public void update(String username, @Valid VehicleRequestDto dto) {
        Vehicle vehicle = vehicleRepository.findByPolicyHolderUserUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Vehicle is invalid"));

        vehicle.setVehicleNumber(dto.vehicleNumber());
        vehicle.setVehicleType(dto.vehicleType());
        vehicle.setVehicleModel(dto.vehicleModel());
        vehicle.setYear(dto.year());

        vehicleRepository.save(vehicle);

    }

    public UploadResponseDto uploadImage(long vehicleId, MultipartFile imageFile) throws IOException {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        uploadUtility.validateImage(imageFile);

        // Resolve the file using Nio : Convert the upload directory into a Path.
        Path uPathDir =  Paths.get(uploadPath);

        // Resolve the file into a path -- target
        Path filePath =  uPathDir.resolve(Objects.requireNonNull(imageFile.getOriginalFilename()));

        // upload the file
        Files.copy(imageFile.getInputStream(), filePath , StandardCopyOption.REPLACE_EXISTING);

        vehicle.setImageUrl(filePath.toString());


        vehicle = vehicleRepository.save(vehicle);

        return new UploadResponseDto(
                vehicle.getId(),
                vehicle.getImageUrl(),
                imageFile.getOriginalFilename(),
                "File upload success"
        );
    }

    public List<VehicleResponseDto> getMyVehicles(String username) {

        PolicyHolder policyHolder = policyHolderRepository
                .findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Policy Holder Not Found"));

        List<Vehicle> list = vehicleRepository.findByPolicyHolderId(policyHolder.getId());

        return list.stream()
                .map(VehicleMapper::convertEntityToDto)
                .toList();
    }

    public VehicleResponseDto getById(long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle id not valid"));

        return VehicleMapper.convertEntityToDto(vehicle);
    }
}

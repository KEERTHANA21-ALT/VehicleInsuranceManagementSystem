package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.VehicleRequestDto;
import com.springboot.insurance.dto.response.VehicleResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.VehicleMapper;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.Vehicle;
import com.springboot.insurance.repository.PolicyHolderRepository;
import com.springboot.insurance.repository.VehicleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final PolicyHolderRepository policyHolderRepository;

    public void add(String userName, VehicleRequestDto dto) {

        // Step 1: Fetch PolicyHolder from given policyHolderId
        PolicyHolder policyHolder = policyHolderRepository.findByUserUsername(userName)
                .orElseThrow(()->new ResourceNotFoundException("PolicyHolder Id invalid"));

        // Step 2: Convert dto to Entity
        Vehicle vehicle = VehicleMapper.convertDtoToEntity(dto);

        // Step 3: Attach policyHolder to vehicle
        vehicle.setPolicyHolder(policyHolder);

        vehicle.setActive(true);

        // Step 4: Save vehicle in Db
        vehicleRepository.save(vehicle);
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
}

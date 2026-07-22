package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.VehicleRequestDto;
import com.springboot.insurance.dto.response.VehicleResponseDto;
import com.springboot.insurance.model.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public static Vehicle convertDtoToEntity(VehicleRequestDto dto) {

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(dto.vehicleNumber());
        vehicle.setVehicleType(dto.vehicleType());
        vehicle.setVehicleModel(dto.vehicleModel());
        vehicle.setYear(dto.year());

        return vehicle;
    }

    public static VehicleResponseDto convertEntityToDto(Vehicle vehicle) {

        VehicleResponseDto vehicleResponseDto = new VehicleResponseDto(
                vehicle.getVehicleNumber(),
                vehicle.getVehicleType(),
                vehicle.getVehicleModel(),
                vehicle.getYear()
        );
        return vehicleResponseDto;
    }
}

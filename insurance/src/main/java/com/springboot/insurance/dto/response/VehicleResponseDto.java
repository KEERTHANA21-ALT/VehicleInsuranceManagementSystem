package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.VehicleType;

public record VehicleResponseDto(
        String vehicleNumber,
        VehicleType vehicleType,
        String vehicleModel,
        int year

) {
}

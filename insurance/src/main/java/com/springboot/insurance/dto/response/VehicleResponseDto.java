package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.VehicleType;

public record VehicleResponseDto(
        long id,
        String vehicleNumber,
        VehicleType vehicleType,
        String vehicleModel,
        int vehicleYear,
        String imageUrl


) {
}

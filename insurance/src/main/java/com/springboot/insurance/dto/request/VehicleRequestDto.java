package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.VehicleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleRequestDto(

        @NotBlank(message = "This field should not be Empty")
        String vehicleNumber,

        VehicleType vehicleType,

        @NotBlank(message = "This field should not be Empty")
        String vehicleModel,

        @NotNull(message = "Year should not be empty")
        @Min(value = 1980, message = "Invalid year")
        @Max(value = 2050, message = "Invalid year")
        Integer year // Making it wrapper class for annotations
) {

}

package com.springboot.insurance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddonRequestDto(

        @NotBlank(message = "This field should not be empty")
        String name,

        @NotNull
        double price,

        @NotBlank(message = "This field should not be empty")
        String description
) {
}

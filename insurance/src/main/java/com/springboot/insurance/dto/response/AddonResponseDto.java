package com.springboot.insurance.dto.response;

public record AddonResponseDto(
        Long id,
        String name,
        double price,
        String description
) {
}

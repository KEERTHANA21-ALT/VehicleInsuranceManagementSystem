package com.springboot.insurance.dto.response;

public record TokenResponseDto(
        String token,
        String expiration,
        String role
) {
}

package com.springboot.insurance.dto.response;

import java.time.LocalDate;

public record PolicyHolderResponseDto(

        String name,
        LocalDate dob,
        String phoneNumber,
        String address
) {
}

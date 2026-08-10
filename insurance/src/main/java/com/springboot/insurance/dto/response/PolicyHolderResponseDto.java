package com.springboot.insurance.dto.response;

import java.time.LocalDate;

public record PolicyHolderResponseDto(

        Long id,
        String name,
        String username,
        LocalDate dob,
        String phoneNumber,
        String address,
        Boolean active,
        Boolean deletionRequested
) {
}

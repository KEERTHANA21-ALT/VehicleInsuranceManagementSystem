package com.springboot.insurance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PolicyHolderRequestDto(

        @NotBlank(message = "This field should not be empty")
        @Pattern(regexp = "[a-zA-Z ]+",message = "Please use Alphabets and space")
        @Size(min = 3, message = "We need minimum 3 chars in name")
        String name,

        @NotNull(message = "This field should not be empty")
        LocalDate dob,

        @NotBlank(message = "This field should not be empty")
        String phoneNumber,

        @NotBlank(message = "This field should not be empty")
        String address,

        @NotBlank(message = "This field should not be Empty")
        String username,

        @NotBlank(message = "This field should not be Empty")
        @Size(min = 5, max=15 , message = "Password should've min 5 and max 15 chars")
        String password
) { }

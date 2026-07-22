package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.EmployeeRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeRequestDto(

        @NotBlank(message = "This field should not be Empty")
        String name,

        @NotNull
        EmployeeRole employeeRole,

        @NotBlank(message = "This field should not be Empty")
        String username,

        @NotBlank(message = "This field should not be Empty")
        @Size(min = 5, max=15 , message = "Password should've min 5 and max 15 chars")
        String password

) {
}

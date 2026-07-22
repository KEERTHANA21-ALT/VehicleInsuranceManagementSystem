package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.EmployeeRole;
import com.springboot.insurance.enums.Role;

public record EmployeeResponseDto(
        String name,
        EmployeeRole employeeRole
) {
}

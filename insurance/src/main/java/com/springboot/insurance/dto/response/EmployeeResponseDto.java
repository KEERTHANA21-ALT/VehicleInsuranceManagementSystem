package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.EmployeeRole;
import com.springboot.insurance.enums.Role;

public record EmployeeResponseDto(
        Long id,
        String name,
        EmployeeRole employeeRole,
        String username,
        Boolean isActive
) {
}

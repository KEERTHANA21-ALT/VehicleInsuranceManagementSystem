package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.EmployeeRequestDto;
import com.springboot.insurance.dto.response.EmployeeResponseDto;
import com.springboot.insurance.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public static Employee convertDtoToEntity(EmployeeRequestDto dto) {

        Employee employee = new Employee();

        employee.setName(dto.name());
        employee.setEmployeeRole(dto.employeeRole());

        return employee;
    }

    public static EmployeeResponseDto convertEntityToDto(Employee employee) {

        EmployeeResponseDto responseDto = new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmployeeRole(),
                employee.getUser().getUsername(),
                employee.isActive()
        );

        return responseDto;
    }
}

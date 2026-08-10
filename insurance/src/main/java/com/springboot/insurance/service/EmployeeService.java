package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.EmployeeRequestDto;
import com.springboot.insurance.dto.response.EmployeeResponseDto;
import com.springboot.insurance.enums.EmployeeRole;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.EmployeeMapper;
import com.springboot.insurance.mapper.UserMapper;
import com.springboot.insurance.model.Employee;
import com.springboot.insurance.model.User;
import com.springboot.insurance.repository.EmployeeRepository;
import com.springboot.insurance.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public void add(EmployeeRequestDto dto) {

        // Step 1: Fetch user details from dto and save in DB
        User user = UserMapper.convertDtoToEntity(
                dto.username(),
                dto.password(),
                Role.EMPLOYEE
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setActivated(true);

        userRepository.save(user);


        // Step 2: Fetch the officer details from dto
        Employee employee = EmployeeMapper.convertDtoToEntity(dto);

        // Step 3: Attach user to officer
        employee.setUser(user);

        // Step 4: Save officer in Db
        employeeRepository.save(employee);
    }


    public EmployeeResponseDto getById(long id) {
        Employee employee = employeeRepository.fetchById(id)
                .orElseThrow(()->new ResourceNotFoundException("Employee id invalid"));

        return EmployeeMapper.convertEntityToDto(employee);
    }

    public List<EmployeeResponseDto> getByEmployeeRole(EmployeeRole employeeRole) {

        List<Employee> list = employeeRepository.getByEmployeeRole(employeeRole);
        return list
                .stream()
                .map(EmployeeMapper :: convertEntityToDto)
                .toList();
    }

    // to switch the navbar content according to the emp role
    public Employee getByUser(User user) {
        return employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }


    public void delete(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Employee Id invalid"));

//        employee.setActive(false);
        employee.setActive(!employee.isActive());

        employeeRepository.save(employee);
    }

    public void update(long id, @Valid EmployeeRequestDto dto) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Employee Id invalid"));


        employee.setName(dto.name());
        employee.setEmployeeRole(dto.employeeRole());
        employee.setActive(dto.isActive());

        employeeRepository.save(employee);

    }


    public List<EmployeeResponseDto> getAll() {
        List<Employee> list = employeeRepository.findAll();

        return list
                .stream()
                .map(EmployeeMapper :: convertEntityToDto)
                .toList();
    }

    public void deleteId(long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Employee Id invalid"));

        employee.setActive(false);
        employeeRepository.save(employee);


    }
}

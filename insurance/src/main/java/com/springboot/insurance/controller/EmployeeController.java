package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.EmployeeRequestDto;
import com.springboot.insurance.dto.response.EmployeeResponseDto;
import com.springboot.insurance.enums.EmployeeRole;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /* Body:
    {
       name:""
       officerRole:""
       username:""
       email:""
       password:""
    }

     */
    @PostMapping("/add")
    public void add(@Valid @RequestBody EmployeeRequestDto dto){
        employeeService.add(dto);
    }

    @GetMapping("/get-one/{id}")
    public EmployeeResponseDto getById(@PathVariable long id){
        return employeeService.getById(id);
    }

    // RequestParam: localhost:8080/api/employee/get-byEmployeeRole/employeeRole?employeeRole=CLAIM_MANAGER
    @GetMapping("/get-byEmployeeRole/{employeeRole}")
    public List<EmployeeResponseDto> getByEmployeeRole(@PathVariable  EmployeeRole employeeRole){
        return employeeService.getByEmployeeRole(employeeRole);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        employeeService.delete(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable long id,@Valid @RequestBody EmployeeRequestDto dto){
        employeeService.update(id,dto);
    }
}

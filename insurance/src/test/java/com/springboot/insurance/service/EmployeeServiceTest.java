package com.springboot.insurance.service;


import com.springboot.insurance.dto.request.EmployeeRequestDto;
import com.springboot.insurance.enums.EmployeeRole;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.Employee;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.User;
import com.springboot.insurance.repository.EmployeeRepository;
import com.springboot.insurance.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Employee employee1;
    private Employee employee2;
    private Employee employee3;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void init() {


        user1 = new User(
                1L,
                "john@gmail.com",
                "john123",
                null,
                Role.EMPLOYEE,
                true
        );


        employee1 = new Employee(
                1L,
                "John Doe",
                EmployeeRole.CLAIM_MANAGER,
                true,
                user1
        );



        user2 = new User(
                2L,
                "jack@gmail.com",
                "jack123",
                null,
                Role.EMPLOYEE,
                true
        );


        employee2= new Employee(
                2L,
                "Jack Doe",
                EmployeeRole.INSURANCE_MANAGER,
                true,
                user2
        );

        user3 = new User(
                3L,
                "jacky@gmail.com",
                "jacky123",
                null,
                Role.EMPLOYEE,
                true
        );


        employee3 = new Employee(
                3L,
                "Jacky Doe",
                EmployeeRole.SURVEYOR,
                true,
                user3
        );

    }

    @Test
    public void addTest(){
        when(passwordEncoder.encode("john123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        EmployeeRequestDto dto = new EmployeeRequestDto(
                "John Doe",
                EmployeeRole.CLAIM_MANAGER,
                "john@gmail.com",
                "john123",
                true
        );

        employeeService.add(dto);

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(employeeCaptor.capture());

        Assertions.assertEquals(dto.name(), employeeCaptor.getValue().getName());

        Assertions.assertEquals(dto.employeeRole(), employeeCaptor.getValue().getEmployeeRole());

        Assertions.assertEquals(dto.username(), employeeCaptor.getValue().getUser().getUsername());

        Assertions.assertEquals(Role.EMPLOYEE, employeeCaptor.getValue().getUser().getRole());
        Assertions.assertEquals("encodedPass", employeeCaptor.getValue().getUser().getPassword());

    }


    @Test
    public void getByIdPresent() {

        when(employeeRepository.fetchById(1L)).thenReturn(Optional.of(employee1));
        Assertions.assertEquals("John Doe", employeeService.getById(1L).name());
        verify(employeeRepository, times(1)).fetchById(1L);

    }


    @Test
    public void getByIdAbsent() {


        when(employeeRepository.fetchById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Employee id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeService.getById(10L)
                ).getMessage()
        );

        verify(employeeRepository, times(1)).fetchById(10L);

    }

    @Test
    public void getByEmployeeRoleTest() {

        List<Employee> employeeList = List.of(employee1, employee2);

        when(employeeRepository.getByEmployeeRole(EmployeeRole.CLAIM_MANAGER)).thenReturn(employeeList);

        Assertions.assertEquals(2,employeeService.getByEmployeeRole(EmployeeRole.CLAIM_MANAGER).size());

        verify(employeeRepository, times(1)).getByEmployeeRole(EmployeeRole.CLAIM_MANAGER);

    }

    @Test
    public void deleteTest() {

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
//        doNothing().when(policyHolderRepository).save(policyHolder1);
        when(employeeRepository.save(employee1))
                .thenReturn(employee1);


        employeeService.delete(1L);

        verify(employeeRepository, times(1))
                .save(employee1);

    }

    @Test
    public void deleteInvalidId() {


        when(employeeRepository.findById(5L)).thenReturn(Optional.empty());

        Assertions.assertEquals("Employee Id invalid",

                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () ->employeeService.delete(5L)
                ).getMessage()
        );
        verify(employeeRepository,never()).save(any());

    }

    @Test
    public void updateTest(){
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee1));

        EmployeeRequestDto dto = new EmployeeRequestDto(
                "John J. Doe",
                EmployeeRole.CLAIM_MANAGER,
                "",
                "",
                true
        );
        // I am making an actual call, this must ensure that , fetchById and save method both get called exactly ONCE
        employeeService.update(10L , dto);

        verify(employeeRepository, times(1)).findById(10L);
        verify(employeeRepository, times(1)).save(employee1);

    }

    @Test
    public void updateTestForInvalidEmployeeId() {

        when(employeeRepository.findById(11L)).thenReturn(Optional.empty());
        EmployeeRequestDto dto = new EmployeeRequestDto(
                "John J. Doe",
                EmployeeRole.CLAIM_MANAGER,
                "",
                "",
                true
        );
        Assertions.assertEquals("Employee Id invalid",
                Assertions.assertThrows(ResourceNotFoundException.class,
                                ()->employeeService.update(11L , dto) )
                        .getMessage()
        );

        verify(employeeRepository, times(1)).findById(11L);
        verify(employeeRepository, times(0)).save(employee1);
    }




}

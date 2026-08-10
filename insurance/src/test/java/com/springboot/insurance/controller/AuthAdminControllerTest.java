package com.springboot.insurance.controller;

import com.springboot.insurance.dto.request.AdminDto;
import com.springboot.insurance.dto.request.PolicyHolderRequestDto;
import com.springboot.insurance.dto.response.TokenResponseDto;
import com.springboot.insurance.dto.response.UserResponseDto;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.model.User;
import com.springboot.insurance.service.EmployeeService;
import com.springboot.insurance.service.PolicyHolderService;
import com.springboot.insurance.service.UserService;
import com.springboot.insurance.utility.JwtUtility;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthAdminControllerTest {

    @InjectMocks
    private AuthAdminController authAdminController;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtility jwtUtility;

    @Mock
    private PolicyHolderService policyHolderService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private Principal principal;

    private User user1;

    @BeforeEach
    public void init() {
        user1 = new User(
                1L,
                "john@gmail.com",
                "john123",
                null,
                Role.POLICY_HOLDER,
                true
        );
    }


    @Test
    public void addAdminTest() {

        AdminDto adminDto = new AdminDto(
                "admin@gmail.com",
                "admin123"
        );

        authAdminController.addAdmin(adminDto);

        verify(userService).addAdmin(adminDto);
    }



    @Test
    public void loginNonEmployeeTest() {

        String username = "john@gmail.com";
        String token = "test-token";

        when(principal.getName()).thenReturn(username);
        when(jwtUtility.generateToken(username)).thenReturn(token);
        when(userService.getUserDetails(username)).thenReturn(user1);
        when(jwtUtility.extractExpiration(token)).thenReturn(new Date());

        TokenResponseDto response = authAdminController.login(principal);

        Assertions.assertNotNull(response);

        verify(principal).getName();
        verify(jwtUtility).generateToken(username);
        verify(userService).getUserDetails(username);
        verify(jwtUtility).extractExpiration(token);

        verify(employeeService, never()).getByUser(user1);
    }

    @Test
    public void getUserDetailsTest() {

        String username = "john@gmail.com";

        when(principal.getName()).thenReturn(username);
        when(userService.getUserDetails(username)).thenReturn(user1);

        UserResponseDto response =
                authAdminController.getUserDetails(principal);

        Assertions.assertNotNull(response);

        verify(principal).getName();
        verify(userService).getUserDetails(username);
    }

    
    @Test
    public void policyHolderSignupTest() {

        PolicyHolderRequestDto dto = new PolicyHolderRequestDto(
                "John Doe",
                LocalDate.of(1995, 5, 10),
                "9876543210",
                "Chennai, Tamil Nadu",
                "john@gmail.com",
                "john123"
        );

        authAdminController.PolicyHolderSignup(dto);

        verify(policyHolderService).signup(dto);
    }


}


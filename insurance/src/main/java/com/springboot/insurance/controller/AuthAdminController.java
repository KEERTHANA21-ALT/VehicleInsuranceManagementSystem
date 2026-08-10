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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthAdminController {

    private final UserService userService;
    private final JwtUtility jwtUtility;
    private final PolicyHolderService policyHolderService;
    private final EmployeeService employeeService;

    @PostMapping("/add/admin")
    public void addAdmin(@RequestBody AdminDto adminDto){
        userService.addAdmin(adminDto);
    }

    // Before the user hits this /login api here in controller, spring would have already checked credentials
    @GetMapping("/login")
    public TokenResponseDto login(Principal principal){

        String loggedInUsername = principal.getName();

        // Generate the token for this username
        String token = jwtUtility.generateToken(loggedInUsername);

        // fetch user details to pass the role
        User user =  userService.getUserDetails(loggedInUsername);

        String employeeRole = null;

        if (user.getRole() == Role.EMPLOYEE) {
            employeeRole = employeeService
                    .getByUser(user)
                    .getEmployeeRole()
                    .name();
        }

        return new TokenResponseDto(
                token,
                jwtUtility.extractExpiration(token).toString(),
                user.getRole().toString(),
                employeeRole
        );
    }


    @GetMapping("/user-details")
    public UserResponseDto getUserDetails(Principal principal){
//        logger.info("Fetching details of User {}", principal.getName());
        String loggedInUsername = principal.getName();
        User user =  userService.getUserDetails(loggedInUsername);
        return new UserResponseDto(
                loggedInUsername,
                user.getRole().toString()
        );
    }

    @PostMapping("/signup")
    public void PolicyHolderSignup(@Valid  @RequestBody PolicyHolderRequestDto dto){
        policyHolderService.signup(dto);
    }
}

package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.AdminDto;
import com.springboot.insurance.dto.response.TokenResponseDto;
import com.springboot.insurance.model.User;
import com.springboot.insurance.service.UserService;
import com.springboot.insurance.utility.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthAdminController {

    private final UserService userService;
    private final JwtUtility jwtUtility;

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

        return new TokenResponseDto(
                token,
                jwtUtility.extractExpiration(token).toString(),
                user.getRole().toString()
        );
    }
}

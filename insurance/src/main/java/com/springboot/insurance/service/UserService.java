package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.AdminDto;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.exception.InvalidCredentialsException;
import com.springboot.insurance.model.User;
import com.springboot.insurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addAdmin(AdminDto adminDto) {

        User user = new User();

        user.setUsername(adminDto.username());
        user.setPassword(passwordEncoder.encode(adminDto.password()));
        user.setRole(Role.ADMIN);
        user.setActivated(true);

        userRepository.save(user);
    }

    public User getUserDetails(String loggedInUsername) {
        return userRepository.loadUserByUsername(loggedInUsername)
                .orElseThrow(()-> new InvalidCredentialsException("Login Denied"));
    }
}

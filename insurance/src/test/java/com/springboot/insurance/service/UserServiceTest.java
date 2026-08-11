package com.springboot.insurance.service;



import com.springboot.insurance.dto.request.AdminDto;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.exception.InvalidCredentialsException;
import com.springboot.insurance.model.User;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user1;

    @BeforeEach
    public void init() {

        user1 = new User(
                1L,
                "admin@gmail.com",
                "admin123",
                null,
                Role.ADMIN,
                true
        );
    }


    @Test
    public void addAdminTest() {

        AdminDto dto = new AdminDto(
                "admin@gmail.com",
                "admin123"
        );

        when(passwordEncoder.encode("admin123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user1);

        userService.addAdmin(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(1)).save(userCaptor.capture());

        Assertions.assertEquals(
                dto.username(),
                userCaptor.getValue().getUsername()
        );

        Assertions.assertEquals(
                "encodedPassword",
                userCaptor.getValue().getPassword()
        );

        Assertions.assertEquals(
                Role.ADMIN,
                userCaptor.getValue().getRole()
        );

        Assertions.assertTrue(userCaptor.getValue().isActivated());

        verify(passwordEncoder, times(1)).encode("admin123");
    }


    @Test
    public void getUserDetailsPresent() {

        when(userRepository.loadUserByUsername("admin@gmail.com")).thenReturn(Optional.of(user1));

        User result = userService.getUserDetails("admin@gmail.com");

        Assertions.assertEquals(
                "admin@gmail.com",
                result.getUsername()
        );

        Assertions.assertEquals(
                Role.ADMIN,
                result.getRole()
        );

        verify(userRepository, times(1)).loadUserByUsername("admin@gmail.com");
    }


    @Test
    public void getUserDetailsAbsent() {

        when(userRepository.loadUserByUsername("invalid@gmail.com")).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Login Denied",
                Assertions.assertThrows(
                        InvalidCredentialsException.class,
                        () -> userService
                                .getUserDetails("invalid@gmail.com")
                ).getMessage()
        );

        verify(userRepository, times(1)).loadUserByUsername("invalid@gmail.com");
    }

}


package com.springboot.insurance.service;

import com.springboot.insurance.model.User;
import com.springboot.insurance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSecurityService userSecurityService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void loadUserByUsername_shouldReturnUser_whenUserExists() {
        when(userRepository.loadUserByUsername("keerthi"))
                .thenReturn(Optional.of(user));

        User result = (User) userSecurityService.loadUserByUsername("keerthi");

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).loadUserByUsername("keerthi");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.loadUserByUsername("keerthi"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userSecurityService.loadUserByUsername("keerthi")
        );

        assertEquals("User Credentials Invalid", exception.getMessage());

        verify(userRepository).loadUserByUsername("keerthi");
    }
}


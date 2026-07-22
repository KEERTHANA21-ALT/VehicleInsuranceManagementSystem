package com.springboot.insurance.service;


import com.springboot.insurance.model.User;
import com.springboot.insurance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.loadUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User Credentials Invalid"));

        return user;
    }
}

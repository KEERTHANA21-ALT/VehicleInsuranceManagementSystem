package com.springboot.insurance.config;


import com.springboot.insurance.service.UserSecurityService;
import com.springboot.insurance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserSecurityService userSecurityService;

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  //.csrf((csrf)->csrf.disable())

                .authorizeHttpRequests(authorize -> authorize

                        // InsurancePlan Apis
                        .requestMatchers(HttpMethod.POST,"/api/insurancePlan/add").hasAuthority("ADMIN")
                        .requestMatchers("/api/insurancePlan/get-one/{id}").permitAll()
                        .requestMatchers("/api/insurancePlan/get-ByPlanType/{planType}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/insurancePlan/update/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/insurancePlan/delete/{id}").hasAuthority("ADMIN")

                        // Vehicle apis
                        .requestMatchers(HttpMethod.POST,"/api/vehicle/add").hasAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/vehicle/get-ByVehicleNumber/{vehicleNumber}").hasAnyAuthority("ADMIN","EMPLOYEE")
                        .requestMatchers("/api/vehicle/get-ByPolicyHolderId/{policyHolderId}").hasAnyAuthority("ADMIN","EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle/update").hasAuthority("POLICY_HOLDER")
                        .requestMatchers(HttpMethod.DELETE,"/api/vehicle/delete/{id}").hasAuthority("ADMIN")

                        // Proposal apis
                        .requestMatchers(HttpMethod.POST,"/api/proposal/add").hasAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/proposal/get-one/{proposalId}").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/proposal/get-myProposals").hasAnyAuthority("POLICY_HOLDER", "EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/proposal/delete/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proposal/assign-employee/{proposalId}/{employeeId}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proposal/update/{id}").hasAnyAuthority("EMPLOYEE","ADMIN")


                        // Payment Apis
                        .requestMatchers(HttpMethod.POST,"/api/payment/add").hasAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/payment/get-one").hasAnyAuthority("POLICY_HOLDER", "EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/payment/get-ByProposalId/{proposalId}").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/payment/update/{id}").hasAnyAuthority("ADMIN","EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE,"/api/payment/delete/{id}").hasAuthority("ADMIN")

                        // Policy Apis
                        .requestMatchers(HttpMethod.POST, "/api/policy/add/{proposalId}").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/policy/get-one/{proposalId}").hasAnyAuthority("EMPLOYEE","ADMIN")
                        .requestMatchers("/api/policy/get-myPolicies").hasAuthority("POLICY_HOLDER")
                        .requestMatchers(HttpMethod.PUT, "/api/policy/update/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/policy/delete/{id}").hasAuthority("ADMIN")

                        // Claim Apis
                        .requestMatchers(HttpMethod.POST,"/api/claim/add").hasAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/claim/get-one").hasAnyAuthority("POLICY_HOLDER","ADMIN","EMPLOYEE")
                        .requestMatchers("/api/claim/get-ByPolicyId").hasAnyAuthority("POLICY_HOLDER","ADMIN","EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/claim/update/{id}").hasAnyAuthority("EMPLOYEE","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/claim/delete/{id}").hasAuthority("ADMIN")


                        // Addon apis
                        .requestMatchers(HttpMethod.POST,"/api/addon/add").hasAuthority("ADMIN")
                        .requestMatchers("/api/addon/get-one/{id}").permitAll()
                        .requestMatchers("/api/addon/get-all").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/addon/update/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/addon/delete/{id}").hasAuthority("ADMIN")


                        // Proposal-Addon apis
                        .requestMatchers(HttpMethod.POST,"/api/proposal-addon/add/{proposalId}/{addonId}").hasAnyAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/proposal-addon/get-proposalId/{proposalId}/addonId/{addonId}").hasAuthority("POLICY_HOLDER")
                        .requestMatchers(HttpMethod.DELETE,"/api/proposal-addon/delete/{id}").hasAuthority("ADMIN")

                        // login api
                        .requestMatchers("/api/auth/login").authenticated()

                       // admin api
                        .requestMatchers(HttpMethod.POST,"/api/auth/add/admin").denyAll() // username:admin , pw:admin@123

                        // Employee Apis
                        .requestMatchers(HttpMethod.POST,"/api/employee/add").hasAuthority("ADMIN") // admin will create employees
                        .requestMatchers("/api/employee/get-one/{id}").hasAuthority("ADMIN")
                        .requestMatchers("/api/employee/get-byEmployeeRole/{employeeRole}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/employee/update/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/employee/delete/{id}").hasAuthority("ADMIN")

                        // PolicyHolder Apis
                        .requestMatchers(HttpMethod.POST,"/api/policyHolder/add").permitAll()
                        .requestMatchers("/api/policyHolder/get-one/{id}").hasAnyAuthority("EMPLOYEE","ADMIN")
                        .requestMatchers("/api/policyHolder/get-all").hasAnyAuthority("EMPLOYEE","ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/policyHolder/update").hasAuthority("POLICY_HOLDER")
                        .requestMatchers(HttpMethod.DELETE,"/api/policyHolder/delete/{id}").hasAuthority("ADMIN")


                        .anyRequest().authenticated())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(userSecurityService);
        dao.setPasswordEncoder(getEncoder());
        return dao;
    }
}

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

                        // Vehicle apis
                        .requestMatchers(HttpMethod.POST,"/api/vehicle/add").hasAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/vehicle/get-ByVehicleNumber/{vehicleNumber}").hasAnyAuthority("ADMIN","EMPLOYEE")
                        .requestMatchers("/api/vehicle/get-ByPolicyHolderId/{policyHolderId}").hasAnyAuthority("ADMIN","EMPLOYEE")

                        // Proposal apis
                        .requestMatchers(HttpMethod.POST,"/api/proposal/add").hasAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/proposal/get-one/{proposalId}").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/proposal/get-myProposals").hasAnyAuthority("POLICY_HOLDER", "EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/proposal/assign-employee/{proposalId}/{employeeId}").hasAuthority("ADMIN")



                        .requestMatchers("/api/payment/get-one/{id}").permitAll()
                        .requestMatchers("/api/payment/get-ByProposalId/{proposalId}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/policy/add/{proposalId}").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/policy/get-one").hasAuthority("POLICY_HOLDER")
                        .requestMatchers("/api/policy/get-myPolicies").hasAuthority("POLICY_HOLDER")

                        .requestMatchers("/api/claim/get-one/{id}").permitAll()
                        .requestMatchers("/api/claim/get-ByPolicyId/{policyId}").permitAll()

                        .requestMatchers("/api/addon/get-one/{id}").permitAll()
                        .requestMatchers("/api/addon/get-all").permitAll()

                        .requestMatchers("/api/proposal-addon/get-proposalId/{proposalId}/addonId/{addonId}").permitAll()

                        // login api
                        .requestMatchers("/api/auth/login").authenticated()

                       // admin api
                        .requestMatchers(HttpMethod.POST,"/api/auth/add/admin").denyAll() // username:admin , pw:admin@123

                        // Sign up apis
                        .requestMatchers(HttpMethod.POST,"/api/employee/add").hasAuthority("ADMIN") // admin will create employees
                        .requestMatchers(HttpMethod.POST,"/api/policyHolder/add").permitAll()


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

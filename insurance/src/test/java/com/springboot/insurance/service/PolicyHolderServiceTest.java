package com.springboot.insurance.service;


import com.springboot.insurance.dto.request.PolicyHolderRequestDto;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.User;
import com.springboot.insurance.repository.PolicyHolderRepository;
import com.springboot.insurance.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PolicyHolderServiceTest {


    @InjectMocks
    private PolicyHolderService policyHolderService;


    @Mock
    private PolicyHolderRepository policyHolderRepository;


    @Mock
    private UserRepository userRepository;


    @Mock
    private PasswordEncoder passwordEncoder;



    private PolicyHolder policyHolder1;
    private PolicyHolder policyHolder2;
    private PolicyHolder policyHolder3;

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
                Role.POLICY_HOLDER,
                true
        );


        policyHolder1 = new PolicyHolder(
                1L,
                "John Doe",
                LocalDate.of(2000, 1, 10),
                "9876543210",
                "Chennai",
                true,
                user1
        );



        user2 = new User(
                2L,
                "jack@gmail.com",
                "jack123",
                null,
                Role.POLICY_HOLDER,
                true
        );


        policyHolder2 = new PolicyHolder(
                2L,
                "Jack Doe",
                LocalDate.of(1999, 5, 20),
                "9876543211",
                "Bangalore",
                true,
                user2
        );

        user3 = new User(
                3L,
                "jacky@gmail.com",
                "jacky123",
                null,
                Role.POLICY_HOLDER,
                true
        );


        policyHolder3 = new PolicyHolder(
                3L,
                "Jacky Doe",
                LocalDate.of(1998, 5, 21),
                "9877543211",
                "Mumbai",
                true,
                user3
        );

    }





    @Test
    public void getByIdPresent() {

        when(policyHolderRepository.fetchById(1L)).thenReturn(Optional.of(policyHolder1));
        Assertions.assertEquals("John Doe", policyHolderService.getById(1L).name());
        verify(policyHolderRepository, times(1)).fetchById(1L);

    }

    @Test
    public void getByIdAbsent() {


        when(policyHolderRepository.fetchById(10L)).thenReturn(Optional.empty());

        Assertions.assertEquals("PolicyHolder is invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> policyHolderService.getById(10L)
                ).getMessage()
        );

        verify(policyHolderRepository, times(1)).fetchById(10L);

    }


    @Test
    public void getAllTest() {
        int page = 0;
        int size=2;
        Pageable pageable = PageRequest.of(page,size);

        Page<PolicyHolder> pagePolicyHolder = new PageImpl<>(List.of(policyHolder1, policyHolder2));
        when(policyHolderRepository.fetchAll(pageable)).thenReturn(pagePolicyHolder);

        size=3;
        Pageable pageable1 =  PageRequest.of(page,size);
        pagePolicyHolder = new PageImpl<>(List.of(policyHolder1,policyHolder2,policyHolder3));

        when(policyHolderRepository.fetchAll(pageable1)).thenReturn(pagePolicyHolder);

        assertEquals(2, policyHolderService.getAll(0,2).size());
        Assertions.assertEquals(3 , policyHolderService.getAll(0,3).size());
        Assertions.assertThrows(RuntimeException.class, ()-> policyHolderService.getAll(0,0));

        verify(policyHolderRepository, times(1)).fetchAll(pageable);
        verify(policyHolderRepository, times(1)).fetchAll(pageable1);

    }


    @Test
    public void addTest() {


        when(passwordEncoder.encode("john@123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(policyHolderRepository.save(any(PolicyHolder.class))).thenReturn(policyHolder1);


        PolicyHolderRequestDto dto =
                new PolicyHolderRequestDto(

                        "John Doe",
                        LocalDate.of(2000,1,10),
                        "9876543210",
                        "Chennai",
                        "john@gmail.com",
                        "john@123"

                );



        // make the actual service call
        policyHolderService.add(dto);



        // capture the PolicyHolder object passed to save()
        ArgumentCaptor<PolicyHolder> policyHolderCaptor = ArgumentCaptor.forClass(PolicyHolder.class);
        verify(policyHolderRepository, times(1)).save(policyHolderCaptor.capture());



        // verify PolicyHolder details

        Assertions.assertEquals(
                dto.name(),
                policyHolderCaptor.getValue().getName()
        );


        Assertions.assertEquals(
                dto.dob(),
                policyHolderCaptor.getValue().getDob()
        );


        Assertions.assertEquals(
                dto.phoneNumber(),
                policyHolderCaptor.getValue().getPhoneNumber()
        );


        Assertions.assertEquals(
                dto.address(),
                policyHolderCaptor.getValue().getAddress()
        );



        // verify User mapping

        Assertions.assertEquals(
                dto.username(),
                policyHolderCaptor.getValue()
                        .getUser()
                        .getUsername()
        );


        Assertions.assertEquals(Role.POLICY_HOLDER, policyHolderCaptor.getValue().getUser().getRole());
        Assertions.assertEquals("encodedPass", policyHolderCaptor.getValue().getUser().getPassword());

    }


    @Test
    public void deleteTest() {

        when(policyHolderRepository.fetchById(1L)).thenReturn(Optional.of(policyHolder1));
//        doNothing().when(policyHolderRepository).save(policyHolder1);
        when(policyHolderRepository.save(policyHolder1))
                .thenReturn(policyHolder1);


        policyHolderService.delete(1L);

        verify(policyHolderRepository, times(1))
                .save(policyHolder1);

    }

    @Test
    public void deleteInvalidId() {


        when(policyHolderRepository.fetchById(5L)).thenReturn(Optional.empty());

        Assertions.assertEquals("PolicyHolder is invalid",

                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> policyHolderService.delete(5L)
                ).getMessage()
        );
        verify(policyHolderRepository,never()).save(any());

    }


    @Test
    public void updateTest() {


        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));

        PolicyHolderRequestDto dto =
                new PolicyHolderRequestDto(

                        "John ",
                        LocalDate.of(2001,2,2),
                        "9999999999",
                        "Madurai",
                        "john@gmail.com",
                        "password"

                );


        policyHolderService.update("john@gmail.com", dto);



        verify(policyHolderRepository,times(1)).findByUserUsername("john@gmail.com");


        verify(policyHolderRepository,times(1)).save(policyHolder1);

        Assertions.assertEquals(
                "John ", policyHolder1.getName());

    }

    @Test
    public void updateInvalidUsername() {


        when(policyHolderRepository.findByUserUsername("wrong@gmail.com")).thenReturn(Optional.empty());

        PolicyHolderRequestDto dto =
                new PolicyHolderRequestDto(

                        "Wrong",
                        LocalDate.of(2000,1,1),
                        "9999999999",
                        "Chennai",
                        "wrong@gmail.com",
                        "password"

                );



        Assertions.assertEquals(

                "PolicyHolder is invalid",

                Assertions.assertThrows(
                        ResourceNotFoundException.class,

                        () -> policyHolderService.update(
                                "wrong@gmail.com",
                                dto
                        )

                ).getMessage()

        );



        verify(policyHolderRepository, times(1)).findByUserUsername("wrong@gmail.com");
        verify(policyHolderRepository, times(0)).save(policyHolder1);

    }


}
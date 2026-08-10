package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.PolicyHolderRequestDto;
import com.springboot.insurance.dto.response.PolicyHolderResponseDto;
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
        user1=new User(1L,"john@gmail.com","john123",null,Role.POLICY_HOLDER,true);
        policyHolder1=new PolicyHolder(1L,"John Doe",LocalDate.of(2000,1,10),"9876543210","Chennai",true,false,user1);
        user2=new User(2L,"jack@gmail.com","jack123",null,Role.POLICY_HOLDER,true);
        policyHolder2=new PolicyHolder(2L,"Jack Doe",LocalDate.of(1999,5,20),"9876543211","Bangalore",true,false,user2);
        user3=new User(3L,"jacky@gmail.com","jacky123",null,Role.POLICY_HOLDER,true);
        policyHolder3=new PolicyHolder(3L,"Jacky Doe",LocalDate.of(1998,5,21),"9877543211","Mumbai",true,false,user3);
    }

    @Test
    public void getByIdPresent() {
        when(policyHolderRepository.fetchById(1L)).thenReturn(Optional.of(policyHolder1));
        Assertions.assertEquals("John Doe",policyHolderService.getById(1L).name());
        verify(policyHolderRepository,times(1)).fetchById(1L);
    }

    @Test
    public void getByIdAbsent() {
        when(policyHolderRepository.fetchById(10L)).thenReturn(Optional.empty());
        Assertions.assertEquals("PolicyHolder is invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->policyHolderService.getById(10L)).getMessage());
        verify(policyHolderRepository,times(1)).fetchById(10L);
    }

    @Test
    public void getAllTest() {
        Pageable pageable=PageRequest.of(0,2);
        Page<PolicyHolder> pagePolicyHolder=new PageImpl<>(List.of(policyHolder1,policyHolder2));
        when(policyHolderRepository.findAll(pageable)).thenReturn(pagePolicyHolder);
        Pageable pageable1=PageRequest.of(0,3);
        pagePolicyHolder=new PageImpl<>(List.of(policyHolder1,policyHolder2,policyHolder3));
        when(policyHolderRepository.findAll(pageable1)).thenReturn(pagePolicyHolder);
        assertEquals(2,policyHolderService.getAll(0,2).size());
        Assertions.assertEquals(3,policyHolderService.getAll(0,3).size());
        Assertions.assertThrows(RuntimeException.class,()->policyHolderService.getAll(0,0));
        verify(policyHolderRepository,times(1)).findAll(pageable);
        verify(policyHolderRepository,times(1)).findAll(pageable1);
    }

    @Test
    public void addTest() {
        when(passwordEncoder.encode("john@123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(policyHolderRepository.save(any(PolicyHolder.class))).thenReturn(policyHolder1);
        PolicyHolderRequestDto dto=new PolicyHolderRequestDto("John Doe",LocalDate.of(2000,1,10),"9876543210","Chennai","john@gmail.com","john@123");
        policyHolderService.add(dto);
        ArgumentCaptor<PolicyHolder> policyHolderCaptor=ArgumentCaptor.forClass(PolicyHolder.class);
        verify(policyHolderRepository,times(1)).save(policyHolderCaptor.capture());
        Assertions.assertEquals(dto.name(),policyHolderCaptor.getValue().getName());
        Assertions.assertEquals(dto.dob(),policyHolderCaptor.getValue().getDob());
        Assertions.assertEquals(dto.phoneNumber(),policyHolderCaptor.getValue().getPhoneNumber());
        Assertions.assertEquals(dto.address(),policyHolderCaptor.getValue().getAddress());
        Assertions.assertEquals(dto.username(),policyHolderCaptor.getValue().getUser().getUsername());
        Assertions.assertEquals(Role.POLICY_HOLDER,policyHolderCaptor.getValue().getUser().getRole());
        Assertions.assertEquals("encodedPass",policyHolderCaptor.getValue().getUser().getPassword());
    }

    @Test
    public void deleteTest() {
        when(policyHolderRepository.findById(1L)).thenReturn(Optional.of(policyHolder1));
        when(policyHolderRepository.save(policyHolder1)).thenReturn(policyHolder1);
        policyHolderService.delete(1L);
        Assertions.assertFalse(policyHolder1.isActive());
        Assertions.assertFalse(policyHolder1.isDeletionRequested());
        Assertions.assertFalse(user1.isActivated());
        verify(policyHolderRepository,times(1)).findById(1L);
        verify(policyHolderRepository,times(1)).save(policyHolder1);
        verify(userRepository,times(1)).save(user1);
    }

    @Test
    public void deleteInvalidId() {
        when(policyHolderRepository.findById(5L)).thenReturn(Optional.empty());
        Assertions.assertEquals("PolicyHolder is invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->policyHolderService.delete(5L)).getMessage());
        verify(policyHolderRepository,never()).save(any());
        verify(userRepository,never()).save(any());
    }

    @Test
    public void updateTest() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        PolicyHolderRequestDto dto=new PolicyHolderRequestDto("John ",LocalDate.of(2001,2,2),"9999999999","Madurai","john@gmail.com","password");
        policyHolderService.update("john@gmail.com",dto);
        verify(policyHolderRepository,times(1)).findByUserUsername("john@gmail.com");
        verify(policyHolderRepository,times(1)).save(policyHolder1);
        Assertions.assertEquals("John ",policyHolder1.getName());
        Assertions.assertEquals(LocalDate.of(2001,2,2),policyHolder1.getDob());
        Assertions.assertEquals("9999999999",policyHolder1.getPhoneNumber());
        Assertions.assertEquals("Madurai",policyHolder1.getAddress());
    }

    @Test
    public void updateInvalidUsername() {
        when(policyHolderRepository.findByUserUsername("wrong@gmail.com")).thenReturn(Optional.empty());
        PolicyHolderRequestDto dto=new PolicyHolderRequestDto("Wrong",LocalDate.of(2000,1,1),"9999999999","Chennai","wrong@gmail.com","password");
        Assertions.assertEquals("PolicyHolder is invalid",Assertions.assertThrows(ResourceNotFoundException.class,()->policyHolderService.update("wrong@gmail.com",dto)).getMessage());
        verify(policyHolderRepository,times(1)).findByUserUsername("wrong@gmail.com");
        verify(policyHolderRepository,never()).save(any());
    }

    @Test
    public void signupTest() {
        when(userRepository.existsByUsername("john@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("john@123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(policyHolderRepository.save(any(PolicyHolder.class))).thenReturn(policyHolder1);
        PolicyHolderRequestDto dto=new PolicyHolderRequestDto("John Doe",LocalDate.of(2000,1,10),"9876543210","Chennai","john@gmail.com","john@123");
        policyHolderService.signup(dto);
        verify(userRepository,times(1)).existsByUsername("john@gmail.com");
        verify(userRepository,times(1)).save(any(User.class));
        verify(policyHolderRepository,times(1)).save(any(PolicyHolder.class));
        ArgumentCaptor<User> userCaptor=ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        Assertions.assertEquals("john@gmail.com",userCaptor.getValue().getUsername());
        Assertions.assertEquals("encodedPass",userCaptor.getValue().getPassword());
        Assertions.assertEquals(Role.POLICY_HOLDER,userCaptor.getValue().getRole());
        Assertions.assertTrue(userCaptor.getValue().isActivated());
    }

    @Test
    public void signupUsernameAlreadyExists() {
        when(userRepository.existsByUsername("john@gmail.com")).thenReturn(true);
        PolicyHolderRequestDto dto=new PolicyHolderRequestDto("John Doe",LocalDate.of(2000,1,10),"9876543210","Chennai","john@gmail.com","john@123");
        Assertions.assertEquals("Username already exists",Assertions.assertThrows(RuntimeException.class,()->policyHolderService.signup(dto)).getMessage());
        verify(userRepository,times(1)).existsByUsername("john@gmail.com");
        verify(userRepository,never()).save(any(User.class));
        verify(policyHolderRepository,never()).save(any(PolicyHolder.class));
    }

    @Test
    public void policyHolderProfileTest() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        Assertions.assertEquals("John Doe",policyHolderService.policyHolderProfile("john@gmail.com").name());
        verify(policyHolderRepository,times(1)).findByUserUsername("john@gmail.com");
    }

    @Test
    public void policyHolderProfileInvalidUsername() {
        when(policyHolderRepository.findByUserUsername("wrong@gmail.com")).thenReturn(Optional.empty());
        Assertions.assertEquals("Invalid user profile",Assertions.assertThrows(ResourceNotFoundException.class,()->policyHolderService.policyHolderProfile("wrong@gmail.com")).getMessage());
        verify(policyHolderRepository,times(1)).findByUserUsername("wrong@gmail.com");
    }

    @Test
    public void requestDeletionTest() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(policyHolderRepository.save(policyHolder1)).thenReturn(policyHolder1);
        policyHolderService.requestDeletion("john@gmail.com");
        Assertions.assertTrue(policyHolder1.isDeletionRequested());
        verify(policyHolderRepository,times(1)).findByUserUsername("john@gmail.com");
        verify(policyHolderRepository,times(1)).save(policyHolder1);
    }

    @Test
    public void requestDeletionInvalidUsername() {
        when(policyHolderRepository.findByUserUsername("wrong@gmail.com")).thenReturn(Optional.empty());
        Assertions.assertEquals("Invalid user profile",Assertions.assertThrows(ResourceNotFoundException.class,()->policyHolderService.requestDeletion("wrong@gmail.com")).getMessage());
        verify(policyHolderRepository,times(1)).findByUserUsername("wrong@gmail.com");
        verify(policyHolderRepository,never()).save(any());
    }

    @Test
    public void getDeletionRequestsTest() {
        when(policyHolderRepository.findByDeletionRequestedTrueOrActiveFalse()).thenReturn(List.of(policyHolder1,policyHolder2));
        List<PolicyHolderResponseDto> result=policyHolderService.getDeletionRequests();
        Assertions.assertEquals(2,result.size());
        verify(policyHolderRepository,times(1)).findByDeletionRequestedTrueOrActiveFalse();
    }

    @Test
    public void getDeletionRequestsEmptyTest() {
        when(policyHolderRepository.findByDeletionRequestedTrueOrActiveFalse()).thenReturn(List.of());
        List<PolicyHolderResponseDto> result=policyHolderService.getDeletionRequests();
        Assertions.assertEquals(0,result.size());
        verify(policyHolderRepository,times(1)).findByDeletionRequestedTrueOrActiveFalse();
    }
}


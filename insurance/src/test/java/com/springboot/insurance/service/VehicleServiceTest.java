package com.springboot.insurance.service;


import com.springboot.insurance.dto.request.VehicleRequestDto;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.enums.VehicleType;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.User;
import com.springboot.insurance.model.Vehicle;
import com.springboot.insurance.repository.PolicyHolderRepository;
import com.springboot.insurance.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private PolicyHolderRepository policyHolderRepository;

    private Vehicle vehicle1;
    private Vehicle vehicle2;

    private PolicyHolder policyHolder1;
    private User user1;


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

        policyHolder1 = new PolicyHolder();
        policyHolder1.setId(1L);
        policyHolder1.setUser(user1);

        vehicle1 = new Vehicle(
                1L,
                "TN10GH3456",
                VehicleType.CAR,
                "Hyundai i20",
                2022,
                true,
                policyHolder1
        );

        vehicle2 = new Vehicle(
                2L,
                "TN10GH3654",
                VehicleType.TWO_WHEELER,
                "Yamaha",
                2021,
                true,
                policyHolder1
        );

    }

    @Test
    public void addTest() {

        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));

        VehicleRequestDto dto = new VehicleRequestDto(
                "TN10GH3456",
                VehicleType.CAR,
                "Hyundai i20",
                2022
        );

        vehicleService.add("john@gmail.com", dto);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);

        verify(vehicleRepository, times(1)).save(captor.capture());

        Assertions.assertEquals(dto.vehicleNumber(), captor.getValue().getVehicleNumber());
        Assertions.assertEquals(dto.vehicleType(), captor.getValue().getVehicleType());
        Assertions.assertEquals(dto.vehicleModel(), captor.getValue().getVehicleModel());
        Assertions.assertEquals(dto.year(), captor.getValue().getYear());
        Assertions.assertEquals(policyHolder1, captor.getValue().getPolicyHolder());
    }

    @Test
    public void getByVehicleNumberPresent() {

        when(vehicleRepository.getByVehicleNumber("TN10GH3456")).thenReturn(Optional.of(vehicle1));

        Assertions.assertEquals(
                "TN10GH3456",
                vehicleService.getByVehicleNumber("TN10GH3456").vehicleNumber()
        );

        verify(vehicleRepository, times(1)).getByVehicleNumber("TN10GH3456");
    }

    @Test
    public void getByVehicleNumberAbsent() {

        when(vehicleRepository.getByVehicleNumber("TN10GH3456")).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Vehicle Number invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> vehicleService.getByVehicleNumber("TN10GH3456")
                ).getMessage()
        );

        verify(vehicleRepository, times(1)).getByVehicleNumber("TN10GH3456");
    }

    @Test
    public void getByPolicyHolderTest() {

        List<Vehicle> list = List.of(vehicle1, vehicle2);

        when(vehicleRepository.findByPolicyHolderId(1L)).thenReturn(list);

        Assertions.assertEquals(
                2,
                vehicleService.getByPolicyHolder(1L).size()
        );

        verify(vehicleRepository, times(1)).findByPolicyHolderId(1L);
    }

    @Test
    public void deleteTest() {

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));

        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);

        vehicleService.delete(1L);

        verify(vehicleRepository, times(1)).save(vehicle1);
    }

    @Test
    public void deleteInvalidId() {

        when(vehicleRepository.findById(5L)).thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Vehicle Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> vehicleService.delete(5L)
                ).getMessage()
        );

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    public void updateTest() {

        when(vehicleRepository.findByPolicyHolderUserUsername("john@gmail.com"))
                .thenReturn(Optional.of(vehicle1));

        VehicleRequestDto dto = new VehicleRequestDto(
                "TN99AA9999",
                VehicleType.TWO_WHEELER,
                "Royal Enfield",
                2024
        );

        vehicleService.update("john@gmail.com", dto);

        verify(vehicleRepository, times(1)).findByPolicyHolderUserUsername("john@gmail.com");

        verify(vehicleRepository, times(1)).save(vehicle1);
    }

    @Test
    public void updateInvalidVehicle() {

        when(vehicleRepository.findByPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.empty());

        VehicleRequestDto dto = new VehicleRequestDto(
                "TN99AA9999",
                VehicleType.TWO_WHEELER,
                "Royal Enfield",
                2024
        );

        Assertions.assertEquals(
                "Vehicle is invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> vehicleService.update("john@gmail.com", dto)
                ).getMessage()
        );

        verify(vehicleRepository, times(1)).findByPolicyHolderUserUsername("john@gmail.com");
        verify(vehicleRepository, times(0)).save(vehicle1);
    }
}

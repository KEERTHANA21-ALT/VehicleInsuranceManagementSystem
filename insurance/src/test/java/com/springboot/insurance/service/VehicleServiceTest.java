package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.VehicleRequestDto;
import com.springboot.insurance.dto.response.UploadResponseDto;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.enums.VehicleType;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.User;
import com.springboot.insurance.model.Vehicle;
import com.springboot.insurance.repository.PolicyHolderRepository;
import com.springboot.insurance.repository.VehicleRepository;
import com.springboot.insurance.utility.UploadUtility;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks private VehicleService vehicleService;
    @Mock private VehicleRepository vehicleRepository;
    @Mock private PolicyHolderRepository policyHolderRepository;
    @Mock private UploadUtility uploadUtility;

    private Vehicle vehicle1, vehicle2;
    private PolicyHolder policyHolder1;
    private User user1;

    @BeforeEach
    public void init() {
        user1 = new User(1L, "john@gmail.com", "john123", null, Role.POLICY_HOLDER, true);
        policyHolder1 = new PolicyHolder();
        policyHolder1.setId(1L);
        policyHolder1.setUser(user1);

        vehicle1 = new Vehicle(1L, "TN10GH3456", VehicleType.CAR, "Hyundai i20", 2022, true, "vehicle1.jpg", policyHolder1);
        vehicle2 = new Vehicle(2L, "TN10GH3654", VehicleType.TWO_WHEELER, "Yamaha", 2021, true, "vehicle2.jpg", policyHolder1);
    }

    @Test
    public void addTest() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle1);

        VehicleRequestDto dto = new VehicleRequestDto("TN10GH3456", VehicleType.CAR, "Hyundai i20", 2022);
        vehicleService.add("john@gmail.com", dto);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(policyHolderRepository).findByUserUsername("john@gmail.com");
        verify(vehicleRepository).save(captor.capture());

        Vehicle savedVehicle = captor.getValue();
        Assertions.assertEquals(dto.vehicleNumber(), savedVehicle.getVehicleNumber());
        Assertions.assertEquals(dto.vehicleType(), savedVehicle.getVehicleType());
        Assertions.assertEquals(dto.vehicleModel(), savedVehicle.getVehicleModel());
        Assertions.assertEquals(dto.year(), savedVehicle.getYear());
        Assertions.assertEquals(policyHolder1, savedVehicle.getPolicyHolder());
        Assertions.assertTrue(savedVehicle.isActive());
    }

    @Test
    public void addInvalidPolicyHolder() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.empty());

        VehicleRequestDto dto = new VehicleRequestDto("TN10GH3456", VehicleType.CAR, "Hyundai i20", 2022);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.add("john@gmail.com", dto));

        Assertions.assertEquals("PolicyHolder Id invalid", exception.getMessage());
        verify(policyHolderRepository).findByUserUsername("john@gmail.com");
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    public void getByVehicleNumberPresent() {
        when(vehicleRepository.getByVehicleNumber("TN10GH3456")).thenReturn(Optional.of(vehicle1));

        Assertions.assertEquals("TN10GH3456", vehicleService.getByVehicleNumber("TN10GH3456").vehicleNumber());
        verify(vehicleRepository).getByVehicleNumber("TN10GH3456");
    }

    @Test
    public void getByVehicleNumberAbsent() {
        when(vehicleRepository.getByVehicleNumber("TN10GH3456")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.getByVehicleNumber("TN10GH3456"));

        Assertions.assertEquals("Vehicle Number invalid", exception.getMessage());
        verify(vehicleRepository).getByVehicleNumber("TN10GH3456");
    }

    @Test
    public void getByPolicyHolderTest() {
        when(vehicleRepository.findByPolicyHolderId(1L)).thenReturn(List.of(vehicle1, vehicle2));

        Assertions.assertEquals(2, vehicleService.getByPolicyHolder(1L).size());
        verify(vehicleRepository).findByPolicyHolderId(1L);
    }

    @Test
    public void getByPolicyHolderEmptyTest() {
        when(vehicleRepository.findByPolicyHolderId(1L)).thenReturn(List.of());

        Assertions.assertEquals(0, vehicleService.getByPolicyHolder(1L).size());
        verify(vehicleRepository).findByPolicyHolderId(1L);
    }

    @Test
    public void deleteTest() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);

        vehicleService.delete(1L);

        Assertions.assertFalse(vehicle1.isActive());
        verify(vehicleRepository).findById(1L);
        verify(vehicleRepository).save(vehicle1);
    }

    @Test
    public void deleteInvalidId() {
        when(vehicleRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.delete(10L));

        Assertions.assertEquals("Vehicle Id invalid", exception.getMessage());
        verify(vehicleRepository).findById(10L);
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    public void updateTest() {
        when(vehicleRepository.findByPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.of(vehicle1));

        VehicleRequestDto dto = new VehicleRequestDto("TN99AA9999", VehicleType.TWO_WHEELER, "Royal Enfield", 2024);
        vehicleService.update("john@gmail.com", dto);

        Assertions.assertEquals("TN99AA9999", vehicle1.getVehicleNumber());
        Assertions.assertEquals(VehicleType.TWO_WHEELER, vehicle1.getVehicleType());
        Assertions.assertEquals("Royal Enfield", vehicle1.getVehicleModel());
        Assertions.assertEquals(2024, vehicle1.getYear());

        verify(vehicleRepository).findByPolicyHolderUserUsername("john@gmail.com");
        verify(vehicleRepository).save(vehicle1);
    }

    @Test
    public void updateInvalidVehicle() {
        when(vehicleRepository.findByPolicyHolderUserUsername("john@gmail.com")).thenReturn(Optional.empty());

        VehicleRequestDto dto = new VehicleRequestDto("TN99AA9999", VehicleType.TWO_WHEELER, "Royal Enfield", 2024);

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.update("john@gmail.com", dto));

        Assertions.assertEquals("Vehicle is invalid", exception.getMessage());
        verify(vehicleRepository).findByPolicyHolderUserUsername("john@gmail.com");
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    public void getMyVehiclesTest() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.findByPolicyHolderId(1L)).thenReturn(List.of(vehicle1, vehicle2));

        Assertions.assertEquals(2, vehicleService.getMyVehicles("john@gmail.com").size());
        verify(policyHolderRepository).findByUserUsername("john@gmail.com");
        verify(vehicleRepository).findByPolicyHolderId(1L);
    }

    @Test
    public void getMyVehiclesEmptyTest() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.of(policyHolder1));
        when(vehicleRepository.findByPolicyHolderId(1L)).thenReturn(List.of());

        Assertions.assertEquals(0, vehicleService.getMyVehicles("john@gmail.com").size());
        verify(policyHolderRepository).findByUserUsername("john@gmail.com");
        verify(vehicleRepository).findByPolicyHolderId(1L);
    }

    @Test
    public void getMyVehiclesInvalidPolicyHolder() {
        when(policyHolderRepository.findByUserUsername("john@gmail.com")).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> vehicleService.getMyVehicles("john@gmail.com"));

        Assertions.assertEquals("Policy Holder Not Found", exception.getMessage());
        verify(policyHolderRepository).findByUserUsername("john@gmail.com");
        verify(vehicleRepository, never()).findByPolicyHolderId(anyLong());
    }

    @Test
    public void getByIdPresent() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));

        Assertions.assertEquals("TN10GH3456", vehicleService.getById(1L).vehicleNumber());
        verify(vehicleRepository).findById(1L);
    }

    @Test
    public void getByIdAbsent() {

        when(vehicleRepository.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> vehicleService.getById(10L));

        Assertions.assertEquals("Vehicle id not valid", exception.getMessage());
        verify(vehicleRepository).findById(10L);
    }



    @Test
    public void uploadImageTest() throws Exception {

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.save(vehicle1)).thenReturn(vehicle1);

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "vehicle.jpg",
                "image/jpeg",
                "test image".getBytes()
        );

        doNothing().when(uploadUtility).validateImage(imageFile);

        UploadResponseDto response = vehicleService.uploadImage(1L, imageFile);

        Assertions.assertEquals(1L, response.id());
        Assertions.assertEquals("vehicle.jpg", response.fileName());
        Assertions.assertEquals("File upload success", response.message());
        Assertions.assertNotNull(response.path());

        Assertions.assertEquals(
                response.path(),
                vehicle1.getImageUrl()
        );

        verify(vehicleRepository).findById(1L);
        verify(uploadUtility).validateImage(imageFile);
        verify(vehicleRepository).save(vehicle1);

        Path uploadedFile = Paths.get(response.path());

        Files.deleteIfExists(uploadedFile);
    }

    @Test
    public void uploadImageInvalidVehicle() throws Exception {

        when(vehicleRepository.findById(10L)).thenReturn(Optional.empty());

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "vehicle.jpg",
                "image/jpeg",
                "test image".getBytes()
        );

        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                        () -> vehicleService.uploadImage(10L, imageFile)
                );

        Assertions.assertEquals(
                "Product not found",
                exception.getMessage()
        );

        verify(vehicleRepository).findById(10L);
        verify(uploadUtility, never()).validateImage(any());
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    public void uploadImageValidationFailed() throws Exception {

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "vehicle.txt",
                "text/plain",
                "test file".getBytes()
        );

        doThrow(new IllegalArgumentException("Invalid image")).when(uploadUtility).validateImage(imageFile);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> vehicleService.uploadImage(1L, imageFile)
        );

        verify(vehicleRepository).findById(1L);
        verify(uploadUtility).validateImage(imageFile);
        verify(vehicleRepository, never()).save(any());
    }
}


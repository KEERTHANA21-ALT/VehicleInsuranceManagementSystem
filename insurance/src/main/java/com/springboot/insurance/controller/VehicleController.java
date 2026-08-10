package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.VehicleRequestDto;
import com.springboot.insurance.dto.response.UploadResponseDto;
import com.springboot.insurance.dto.response.VehicleResponseDto;
import com.springboot.insurance.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class VehicleController {

    private final VehicleService vehicleService;


    /*
    Body{
    vehicleNumber:""
    vehicleType:""
    vehicleModel:""
    year:""
    }
     */
        @PostMapping("/add")
        public VehicleResponseDto add(Principal principal, @Valid @RequestBody VehicleRequestDto dto){
            String userName = principal.getName();
            return vehicleService.add(userName,dto);
        }

    // Path Variable : http://localhost:8080/api/vehicle/get-ByVehicleNumber/TN10GH3456
    @GetMapping("/get-ByVehicleNumber/{vehicleNumber}")
    public VehicleResponseDto getByVehicleNumber(@PathVariable String vehicleNumber){
        return vehicleService.getByVehicleNumber(vehicleNumber);
    }

    // Using list becoz one holder can have insurance for many vehicle
    @GetMapping("/get-ByPolicyHolder/{policyHolderId}")
    public List<VehicleResponseDto> getByPolicyHolder(@PathVariable long policyHolderId){
        return vehicleService.getByPolicyHolder(policyHolderId);
    }

    @GetMapping("/get-myVehicles")
    public List<VehicleResponseDto> getMyVehicles(Principal principal) {
        String username = principal.getName();
        return vehicleService.getMyVehicles(username    );
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        vehicleService.delete(id);
    }

    @PutMapping("/update")
    public void update(Principal principal, @Valid @RequestBody VehicleRequestDto dto){
        String username = principal.getName();
        vehicleService.update(username,dto);
    }

    @PostMapping("/image/upload/{vehicleId}")
    public UploadResponseDto uploadImage(@PathVariable long vehicleId,
                                         @RequestParam("vImage") MultipartFile imageFile) throws IOException, InterruptedException{
        return vehicleService.uploadImage(vehicleId,imageFile);
    }


    @GetMapping("/{id}")
    public VehicleResponseDto getById(@PathVariable long id){
            return vehicleService.getById(id);
    }
}

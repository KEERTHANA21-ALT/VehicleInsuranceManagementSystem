package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.VehicleRequestDto;
import com.springboot.insurance.dto.response.VehicleResponseDto;
import com.springboot.insurance.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
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
    public void add(Principal principal, @Valid @RequestBody VehicleRequestDto dto){
        String userName = principal.getName();
        vehicleService.add(userName,dto);
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

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        vehicleService.delete(id);
    }

    @PutMapping("/update")
    public void update(Principal principal, @Valid @RequestBody VehicleRequestDto dto){
        String username = principal.getName();
        vehicleService.update(username,dto);
    }
}

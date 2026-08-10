package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.InsurancePlanRequestDto;
import com.springboot.insurance.dto.response.InsurancePlanResponseDto;
import com.springboot.insurance.dto.response.VehicleResponseDto;
import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.service.InsurancePlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurancePlan")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InsurancePlanController {

    private final InsurancePlanService insurancePlanService;

    @PostMapping("/add")
    public void add(@Valid @RequestBody InsurancePlanRequestDto dto){
        insurancePlanService.add(dto);
    }

    @GetMapping("/get-one/{id}")
    public InsurancePlanResponseDto getById(@PathVariable long id){
        return insurancePlanService.getById(id);
    }

    @GetMapping("/get-ByPlanType/{planType}")
    public List<InsurancePlanResponseDto> getByPlanType(@PathVariable PlanType planType){
        return insurancePlanService.getByPlanType(planType);
    }

    @GetMapping("/get-all")
    public List<InsurancePlanResponseDto> getAll(){
        return insurancePlanService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        insurancePlanService.delete(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable long id, @Valid @RequestBody InsurancePlanRequestDto dto){
        insurancePlanService.update(id,dto);

    }

    @PutMapping("toggle/{id}")
    public void updateToggle(@PathVariable long id){
        insurancePlanService.updateToggle(id);
    }


}

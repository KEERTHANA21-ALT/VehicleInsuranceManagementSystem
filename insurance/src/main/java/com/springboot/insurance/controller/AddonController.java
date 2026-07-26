package com.springboot.insurance.controller;


import com.springboot.insurance.dto.request.AddonRequestDto;
import com.springboot.insurance.dto.response.AddonResponseDto;
import com.springboot.insurance.dto.response.PolicyHolderResponseDto;
import com.springboot.insurance.service.AddonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addon")
@RequiredArgsConstructor
public class AddonController {

    private final AddonService addonService;

    @PostMapping("/add")
    public void add(@Valid @RequestBody AddonRequestDto dto){
        addonService.add(dto);
    }

    @GetMapping("/get-one/{id}")
    public AddonResponseDto getById(@PathVariable long id){
        return addonService.getById(id);
    }

    @GetMapping("/get-all")
    public List<AddonResponseDto> getAll(@RequestParam Integer page, @RequestParam Integer size){
        return addonService.getAll(page,size);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable long id){
        addonService.delete(id);
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable long id, @Valid @RequestBody AddonRequestDto dto){
        addonService.update(id,dto);
    }
}

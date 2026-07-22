package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.AddonRequestDto;
import com.springboot.insurance.dto.response.AddonResponseDto;
import com.springboot.insurance.model.Addon;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class AddonMapper {

    public static Addon convertDtoToEntity(@Valid AddonRequestDto dto) {
        Addon addon = new Addon();

        addon.setName(dto.name());
        addon.setPrice(dto.price());
        addon.setDescription(dto.description());

        return addon;
    }


    public static AddonResponseDto convertEntityToDto(Addon addon) {

        AddonResponseDto addonResponseDto = new AddonResponseDto(
                addon.getName(),
                addon.getPrice(),
                addon.getDescription()
        );
        return addonResponseDto;
    }
}

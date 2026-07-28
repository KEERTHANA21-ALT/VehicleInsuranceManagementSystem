package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.AddonRequestDto;
import com.springboot.insurance.dto.response.AddonResponseDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.AddonMapper;
import com.springboot.insurance.model.Addon;
import com.springboot.insurance.repository.AddonRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AddonService {

    private final AddonRepository addonRepository;

    public void add(@Valid AddonRequestDto dto) {
        Addon addon = AddonMapper.convertDtoToEntity(dto);

        addon.setActive(true);

        addonRepository.save(addon);
    }

    public AddonResponseDto getById(long id) {
        Addon addon = addonRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Addon id Invalid"));

        return AddonMapper.convertEntityToDto(addon);
    }

    public List<AddonResponseDto> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        List<Addon> list = addonRepository.findAll(pageable).getContent();

        return list
                .stream()
                .map(AddonMapper :: convertEntityToDto)
                .toList();
    }

    public void delete(long id) {
        Addon addon = addonRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Addon Id invalid"));

        addon.setActive(false);

        addonRepository.save(addon);
    }

    public void update(long id, @Valid AddonRequestDto dto) {

        Addon addon = addonRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Addon Id invalid"));

        addon.setName(dto.name());
        addon.setPrice(dto.price());
        addon.setDescription(dto.description());

        addonRepository.save(addon);
    }
}



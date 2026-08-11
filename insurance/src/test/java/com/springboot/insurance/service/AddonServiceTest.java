package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.AddonRequestDto;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.model.Addon;
import com.springboot.insurance.repository.AddonRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddonServiceTest {

    @InjectMocks
    private AddonService addonService;

    @Mock
    private AddonRepository addonRepository;

    private Addon addon1;
    private Addon addon2;

    @BeforeEach
    public void init() {

        addon1 = new Addon(
                1L,
                "ZERO_DEPRECIATION",
                2500,
                "Covers full depreciation",
                true
        );

        addon2 = new Addon(
                2L,
                "ENGINE_PROTECTION",
                1500,
                "Protects engine against damage",
                true
        );
    }

    @Test
    public void addTest() {

        AddonRequestDto dto = new AddonRequestDto(
                "ZERO_DEPRECIATION",
                2500,
                "Covers full depreciation"
        );

        addonService.add(dto);

        ArgumentCaptor<Addon> captor =
                ArgumentCaptor.forClass(Addon.class);

        verify(addonRepository, times(1))
                .save(captor.capture());

        Assertions.assertEquals(
                dto.name(),
                captor.getValue().getName()
        );

        Assertions.assertEquals(
                dto.price(),
                captor.getValue().getPrice()
        );

        Assertions.assertEquals(
                dto.description(),
                captor.getValue().getDescription()
        );

        Assertions.assertTrue(
                captor.getValue().isActive()
        );
    }


    @Test
    public void getByIdPresent() {

        when(addonRepository.findById(1L))
                .thenReturn(Optional.of(addon1));

        Assertions.assertEquals(
                "ZERO_DEPRECIATION",
                addonService.getById(1L).name()
        );

        verify(addonRepository, times(1))
                .findById(1L);
    }


    @Test
    public void getByIdAbsent() {

        when(addonRepository.findById(10L))
                .thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Addon id Invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> addonService.getById(10L)
                ).getMessage()
        );

        verify(addonRepository, times(1))
                .findById(10L);
    }


    @Test
    public void getAllTest() {

        int page = 0;
        int size = 2;

        Pageable pageable = PageRequest.of(page, size);

        Page<Addon> pageAddon =
                new PageImpl<>(List.of(addon1, addon2));

        when(addonRepository.findAll(pageable))
                .thenReturn(pageAddon);

        size = 1;

        Pageable pageable1 = PageRequest.of(page, size);

        pageAddon =
                new PageImpl<>(List.of(addon1));

        when(addonRepository.findAll(pageable1))
                .thenReturn(pageAddon);

        Assertions.assertEquals(
                2,
                addonService.getAll(0, 2).size()
        );

        Assertions.assertEquals(
                1,
                addonService.getAll(0, 1).size()
        );

        Assertions.assertThrows(
                RuntimeException.class,
                () -> addonService.getAll(0, 0)
        );

        verify(addonRepository, times(1))
                .findAll(pageable);

        verify(addonRepository, times(1))
                .findAll(pageable1);
    }


    @Test
    public void getAllEmptyTest() {

        int page = 0;
        int size = 2;

        Pageable pageable =
                PageRequest.of(page, size);

        Page<Addon> pageAddon =
                new PageImpl<>(List.of());

        when(addonRepository.findAll(pageable))
                .thenReturn(pageAddon);

        Assertions.assertEquals(
                0,
                addonService.getAll(0, 2).size()
        );

        verify(addonRepository, times(1))
                .findAll(pageable);
    }


    @Test
    public void deleteTest() {

        addon1.setActive(true);

        when(addonRepository.findById(1L))
                .thenReturn(Optional.of(addon1));

        when(addonRepository.save(addon1))
                .thenReturn(addon1);

        addonService.delete(1L);

        Assertions.assertFalse(
                addon1.isActive()
        );

        verify(addonRepository, times(1))
                .findById(1L);

        verify(addonRepository, times(1))
                .save(addon1);
    }


    @Test
    public void deleteInvalidId() {

        when(addonRepository.findById(10L))
                .thenReturn(Optional.empty());

        Assertions.assertEquals(
                "Addon Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> addonService.delete(10L)
                ).getMessage()
        );

        verify(addonRepository, never())
                .save(any());
    }


    @Test
    public void updateTest() {

        when(addonRepository.findById(1L))
                .thenReturn(Optional.of(addon1));

        AddonRequestDto dto = new AddonRequestDto(
                "ROADSIDE_ASSISTANCE",
                3000,
                "24x7 Roadside Support"
        );

        addonService.update(1L, dto);

        Assertions.assertEquals(
                "ROADSIDE_ASSISTANCE",
                addon1.getName()
        );

        Assertions.assertEquals(
                3000,
                addon1.getPrice()
        );

        Assertions.assertEquals(
                "24x7 Roadside Support",
                addon1.getDescription()
        );

        verify(addonRepository, times(1))
                .findById(1L);

        verify(addonRepository, times(1))
                .save(addon1);
    }


    @Test
    public void updateInvalidId() {

        when(addonRepository.findById(10L))
                .thenReturn(Optional.empty());

        AddonRequestDto dto = new AddonRequestDto(
                "ROADSIDE_ASSISTANCE",
                3000,
                "24x7 Roadside Support"
        );

        Assertions.assertEquals(
                "Addon Id invalid",
                Assertions.assertThrows(
                        ResourceNotFoundException.class,
                        () -> addonService.update(10L, dto)
                ).getMessage()
        );

        verify(addonRepository, times(1))
                .findById(10L);

        verify(addonRepository, times(0))
                .save(addon1);
    }

}


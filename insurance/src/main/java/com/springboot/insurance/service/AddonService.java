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
}


//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class ProposalAddon {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//
//    private double addonPrice;
//
//
//    @ManyToOne
//    @JoinColumn(name = "proposal_id", nullable = false)
//    private Proposal proposal;
//
//
//    @ManyToOne
//    @JoinColumn(name = "addon_id", nullable = false)
//    private Addon addon;
//
//}
package com.springboot.insurance.service;

import com.springboot.insurance.dto.request.PolicyHolderRequestDto;
import com.springboot.insurance.dto.response.PolicyHolderResponseDto;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.exception.ResourceNotFoundException;
import com.springboot.insurance.mapper.PolicyHolderMapper;
import com.springboot.insurance.mapper.UserMapper;
import com.springboot.insurance.model.PolicyHolder;
import com.springboot.insurance.model.User;
import com.springboot.insurance.repository.PolicyHolderRepository;
import com.springboot.insurance.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyHolderService {

    private final PolicyHolderRepository policyHolderRepository;
    private final PolicyHolderMapper policyHolderMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void add(PolicyHolderRequestDto dto) {

        // Step 1: Fetch user details from dto and save in DB
        User user = UserMapper.convertDtoToEntity(
                dto.username(),
                dto.password(),
                Role.POLICY_HOLDER
        );

        // encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setActivated(true);

        userRepository.save(user);

        // Step 2: Fetch policyHolder details from dto
        PolicyHolder policyHolder = PolicyHolderMapper.convertDtoToEntity(dto);

        // Step 3: Attach user to policyHolder
        policyHolder.setUser(user);

        // Step 4: Save policyHolder in Db
        policyHolderRepository.save(policyHolder);


    }

    public List<PolicyHolderResponseDto> getAll(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page,size);
        List<PolicyHolder> list = policyHolderRepository.fetchAll(pageable).getContent();
        return list
                .stream()
                .map(PolicyHolderMapper :: convertEntityToDto)
                .toList();
    }

    public PolicyHolderResponseDto getById(long id) {

        PolicyHolder policyHolder = policyHolderRepository.fetchById(id)
                .orElseThrow(()->new ResourceNotFoundException("PolicyHolder is invalid"));

        return PolicyHolderMapper.convertEntityToDto(policyHolder);

    }

    public void delete(long id) {
        PolicyHolder policyHolder = policyHolderRepository.fetchById(id)
                .orElseThrow(()->new ResourceNotFoundException("PolicyHolder is invalid"));

        policyHolder.setActive(false);
        policyHolderRepository.save(policyHolder);
    }


    public void update(String username, @Valid PolicyHolderRequestDto policyHolderRequestDto) {

        PolicyHolder policyHolderDb = policyHolderRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("PolicyHolder is invalid"));

        policyHolderDb.setName(policyHolderRequestDto.name());
        policyHolderDb.setDob(policyHolderRequestDto.dob());
        policyHolderDb.setPhoneNumber(policyHolderRequestDto.phoneNumber());
        policyHolderDb.setAddress(policyHolderRequestDto.address());

        policyHolderRepository.save(policyHolderDb);

    }
}

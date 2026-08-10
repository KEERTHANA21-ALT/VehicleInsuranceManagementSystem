package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.PolicyHolderRequestDto;
import com.springboot.insurance.dto.response.PolicyHolderResponseDto;
import com.springboot.insurance.model.PolicyHolder;
import org.springframework.stereotype.Component;

@Component
public class PolicyHolderMapper {

    public static PolicyHolder convertDtoToEntity(PolicyHolderRequestDto dto) {
        PolicyHolder policyHolder = new PolicyHolder();

        policyHolder.setName(dto.name());
        policyHolder.setDob(dto.dob());
        policyHolder.setPhoneNumber(dto.phoneNumber());
        policyHolder.setAddress(dto.address());

        return policyHolder;

    }

    public static PolicyHolderResponseDto convertEntityToDto(PolicyHolder policyHolder){

        PolicyHolderResponseDto policyHolderResponseDto = new PolicyHolderResponseDto(

                policyHolder.getId(),
                policyHolder.getName(),
                policyHolder.getUser().getUsername(),
                policyHolder.getDob(),
                policyHolder.getPhoneNumber(),
                policyHolder.getAddress(),
                policyHolder.isActive(),
                policyHolder.isDeletionRequested()


        );
        return policyHolderResponseDto;

    }
}

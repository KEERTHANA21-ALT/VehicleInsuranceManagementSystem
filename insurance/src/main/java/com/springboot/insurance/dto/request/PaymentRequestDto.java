package com.springboot.insurance.dto.request;

import com.springboot.insurance.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDto(

        @NotNull(message = "This field should not be Empty")
        PaymentMethod paymentMethod

) {
}

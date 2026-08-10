package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.PaymentMethod;
import com.springboot.insurance.enums.PaymentStatus;

import java.time.Instant;

public record PaymentResponseDto(
        Long id,
        double amount,
        PaymentMethod paymentMethod,
        Instant paymentDate,
        PaymentStatus paymentStatus
) {
}

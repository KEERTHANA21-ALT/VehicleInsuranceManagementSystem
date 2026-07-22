package com.springboot.insurance.dto.response;

import com.springboot.insurance.enums.PaymentMethod;
import com.springboot.insurance.enums.PaymentStatus;

import java.time.Instant;

public record PaymentResponseDto(
        double amount,
        PaymentMethod paymentMethod,
        Instant paymentDate,
        PaymentStatus paymentStatus
) {
}

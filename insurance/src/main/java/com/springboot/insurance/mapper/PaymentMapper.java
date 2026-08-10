package com.springboot.insurance.mapper;

import com.springboot.insurance.dto.request.PaymentRequestDto;
import com.springboot.insurance.dto.response.PaymentResponseDto;
import com.springboot.insurance.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public static Payment convertDtoToEntity(PaymentRequestDto dto) {

        Payment payment = new Payment();

        payment.setPaymentMethod(dto.paymentMethod());

        return payment;
    }

    public static PaymentResponseDto convertEntityToDto(Payment payment) {

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentDate(),
                payment.getPaymentStatus()
        );
        return paymentResponseDto;
    }
}

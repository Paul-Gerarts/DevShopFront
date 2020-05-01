package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.PaymentOption;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;

import java.math.BigDecimal;
import java.util.Arrays;

public class PaymentUtils {

    public static PaymentDto createPaymentDtoWithTotalCartPrice() {
        return PaymentDto.builder()
                .paymentOptions(Arrays.asList(PaymentOption.values()))
                .totalCartPrice(BigDecimal.valueOf(22L))
                .build();
    }
}

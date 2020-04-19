package be.syntra.devshop.DevshopFront.models.dtos;

import be.syntra.devshop.DevshopFront.models.PaymentOption;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class PaymentDto {

    private BigDecimal totalPrice;
    private List<PaymentOption> paymentOptions = new ArrayList<>(List.of(PaymentOption.values()));
}

package be.syntra.devshop.DevshopFront.models.dtos;

import be.syntra.devshop.DevshopFront.models.PaymentOption;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PaymentDto {

    private List<PaymentOption> paymentOptions;
    private BigDecimal totalCartPrice;
}

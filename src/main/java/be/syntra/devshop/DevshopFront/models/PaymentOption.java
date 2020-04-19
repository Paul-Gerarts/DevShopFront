package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentOption {
    PAY("regular payment", "/paycart");

    private final String description;
    private final String link;
}

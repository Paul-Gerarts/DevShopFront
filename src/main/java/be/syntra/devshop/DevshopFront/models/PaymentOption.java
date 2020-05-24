package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentOption {

    REGULAR("regular payment"),
    CREDIT("creditcard payment"),
    WIRE_TRANSFER("wire-transfer payment");

    private final String description;

}

package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusNotification {

    LOGIN_FAIL("Login failed"),
    SUCCES("Succes"),
    SAVED("Saved"),
    ERROR("Error");

    private final String label;

}

package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusNotification {

    PASSWORD_NO_MATCH("The provided passwords don't match!"),
    REGISTER_FAIL("Registering new user failed"),
    SUCCES("Succes"),
    SAVED("Saved"),
    ERROR("Error");

    private final String label;

}

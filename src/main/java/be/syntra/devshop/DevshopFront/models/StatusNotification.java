package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusNotification {

    PASSWORD_NO_MATCH("The provided passwords don't match!"),
    REGISTER_FAIL("Registering new user failed. Try a different email-address"),
    UPDATED("The update was successful!"),
    SUCCESS("Success"),
    SAVED("Saved"),
    ERROR("Error: make sure you fill in the form correctly");

    private final String label;

}

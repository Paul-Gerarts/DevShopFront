package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusNotification {

    PASSWORD_NO_MATCH("The provided passwords don't match!"),
    REGISTER_FAIL("Registering new user failed. Try a different emailaddress"),
    UPDATED("The update was succesful!"),
    SUCCESS("Success"),
    SAVED("Saved"),
    ERROR("Error");

    private final String label;

}

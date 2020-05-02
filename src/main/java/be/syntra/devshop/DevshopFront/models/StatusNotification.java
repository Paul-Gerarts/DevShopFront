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
    FORM_ERROR("Error: make sure you fill in the form correctly"),
    PERSISTANCE_ERROR("Error: something went wrong while persisting the desired object"),
    DELETED("delete successful");

    private final String label;

}

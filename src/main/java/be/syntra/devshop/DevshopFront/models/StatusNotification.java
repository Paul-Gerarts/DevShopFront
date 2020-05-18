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
    FORM_ERROR("Make sure you fill in the form correctly"),
    PERSISTENCE_ERROR("Something went wrong while persisting the desired object"),
    DELETED("delete successful"),
    DELETE_FAIL("deletion failed"),
    PAYMENT_FAIL("Payment cart failed."),
    UPDATE_FAIL("Failed to change the category"),
    RATING_FAIL("Failed to submit the rating"),
    CATEGORY_EXISTS("Creation failed. Category already excists"),
    NOT_AUTHORIZED("You must first login before this functionality becomes available");

    private final String label;

}

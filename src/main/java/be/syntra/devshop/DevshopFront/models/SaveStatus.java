package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SaveStatus {
    SAVED("Saved"),
    ERROR("Error");

    private final String label;

}

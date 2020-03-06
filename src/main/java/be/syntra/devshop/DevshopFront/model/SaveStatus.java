package be.syntra.devshop.DevshopFront.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SaveStatus {
    SAVED("Saved"),
    ERROR("Error");

    private String label;

    public String label() {
        return getLabel();
    }
}

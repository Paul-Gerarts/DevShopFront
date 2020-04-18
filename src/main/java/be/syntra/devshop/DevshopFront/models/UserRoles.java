package be.syntra.devshop.DevshopFront.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRoles {

    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String authority;

}

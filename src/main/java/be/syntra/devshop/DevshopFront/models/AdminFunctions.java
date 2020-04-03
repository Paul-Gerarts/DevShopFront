package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminFunctions {

    ADD_PRODUCT("ADD a product", "note_add", "./addproduct"),
    UPDATE_PRODUCT("UPDATE a product", "sync_alt", "/products"),
    DELETE_PRODUCT("DELETE a product", "delete_forever", "/products");

    private final String description;
    private final String icon;
    private final String link;
}

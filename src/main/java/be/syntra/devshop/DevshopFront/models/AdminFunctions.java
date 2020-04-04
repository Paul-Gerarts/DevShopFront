package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminFunctions {

    ADD_PRODUCT("ADD a product", "note_add", "./addproduct"),
    UPDATE_PRODUCT("UPDATE a product", "sync_alt", "/products"),
    ARCHIVE_PRODUCT("ARCHIVE a product", "archive", "/products");

    private final String description;
    private final String icon;
    private final String link;
}

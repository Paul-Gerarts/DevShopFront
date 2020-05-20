package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.dtos.CartDto;

import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.*;


public class CartUtils {

    public static CartDto getCartWithOneDummyProduct() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .products(List.of(getDummyNonArchivedProduct()))
                .build();
    }

    public static CartDto getCartWithMultipleNonArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .products(getDummyNonArchivedProductList())
                .build();
    }

    public static CartDto getCartWithMultipleArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .products(getDummyArchivedProductList())
                .build();
    }
}

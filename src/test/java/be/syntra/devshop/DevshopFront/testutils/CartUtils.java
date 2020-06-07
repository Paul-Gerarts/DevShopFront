package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.dtos.CartDto;

import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductDto;


public class CartUtils {

    public static CartDto getCartWithOneDummyProduct() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                //.products(List.of(getDummyNonArchivedProduct()))
                .productDtos(List.of(getDummyProductDto()))
                .build();
    }

    public static CartDto getCartWithMultipleNonArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                //.products(getDummyNonArchivedProductList())
                .productDtos(List.of(getDummyProductDto()))
                .build();
    }

    public static CartDto getCartWithMultipleArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                //.products(getDummyArchivedProductList())
                .productDtos(List.of(getDummyProductDto()))
                .build();
    }
}

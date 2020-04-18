package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;

import java.util.List;

public class CartUtils {
    public static CartDto getCartWithOneDummyProduct() {
        return CartDto.builder()
                .activeCart(true)
                .finalizedCart(false)
                .paidCart(false)
                .products(List.of(ProductUtils.getDummyNonArchivedProduct()))
                .build();
    }
}

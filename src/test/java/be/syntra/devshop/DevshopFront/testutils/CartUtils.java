package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.dtos.CartDisplayDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartProductDto;

import java.util.HashSet;
import java.util.Set;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductDto;


public class CartUtils {

    public static CartDto getCartWithOneDummyProduct() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .cartProductDtoSet(getCartProductDtoSet())
                .build();
    }

    public static CartDto getCartWithMultipleNonArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .cartProductDtoSet(getCartProductDtoSet())
                .build();
    }

    public static CartDisplayDto getCartProductsDisplayDto() {
        return CartDisplayDto.builder()
                .cartProductDtoSet(getCartProductDtoSet())
                .cartProductsIdSet(getProductIdSet())
                .build();
    }

    private static Set<Long> getProductIdSet() {
        Set<Long> cartProductsIdList = new HashSet<>();
        cartProductsIdList.add(getDummyCartContentDto().getProductDto().getId());
        return cartProductsIdList;
    }

    private static Set<CartProductDto> getCartProductDtoSet() {
        Set<CartProductDto> cartProductDtoList = new HashSet<>();
        cartProductDtoList.add(getDummyCartContentDto());
        return cartProductDtoList;
    }

    private static CartProductDto getDummyCartContentDto() {
        return CartProductDto.builder()
                .productDto(getDummyProductDto())
                .count(1)
                .build();
    }
}

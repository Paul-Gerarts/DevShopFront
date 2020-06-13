package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.dtos.CartDisplayDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartDisplayProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartProductDto;

import java.util.ArrayList;
import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductDto;


public class CartUtils {

    public static CartDto getCartWithOneDummyProduct() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .cartProductDtoList(getDummyCartContentDtoList())
                .build();
    }

    public static CartDto getCartWithMultipleNonArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .cartProductDtoList(getDummyCartContentDtoList())
                .build();
    }

    private static CartProductDto getDummyCartContentDto() {
        return CartProductDto.builder()
                .productDto(getDummyProductDto())
                .count(1)
                .build();
    }

    private static List<CartProductDto> getDummyCartContentDtoList() {
        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        cartProductDtoList.add(getDummyCartContentDto());
        return cartProductDtoList;
    }

    public static CartDisplayDto getCartProductsDisplayDto() {
        CartDisplayProductDto cartDisplayProductDto = CartDisplayProductDto.builder()
                .product(getDummyProductDto())
                .productCount(1)
                .build();
        List<Long> cartProductsIdList = new ArrayList<>();
        cartProductsIdList.add(cartDisplayProductDto.getProduct().getId());
        List<CartDisplayProductDto> cartDisplayProductDtoList = new ArrayList<>();
        cartDisplayProductDtoList.add(cartDisplayProductDto);
        return CartDisplayDto.builder()
                .cartDisplayProductDtoList(cartDisplayProductDtoList)
                .cartProductsIdList(cartProductsIdList)
                .build();
    }
}

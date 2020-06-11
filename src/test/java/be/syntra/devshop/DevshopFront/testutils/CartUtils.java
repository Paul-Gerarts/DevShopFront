package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.dtos.CartContentDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;

import java.util.ArrayList;
import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductDto;


public class CartUtils {

    public static CartDto getCartWithOneDummyProduct() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .cartContentDtoList(getDummyCartContentDtoList())
                .build();
    }

    public static CartDto getCartWithMultipleNonArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .cartContentDtoList(getDummyCartContentDtoList())
                .build();
    }

    public static CartDto getCartWithMultipleArchivedProducts() {
        return CartDto.builder()
                .finalizedCart(false)
                .paidCart(false)
                .cartContentDtoList(getDummyCartContentDtoList())
                .build();
    }

    private static CartContentDto getDummyCartContentDto() {
        return CartContentDto.builder()
                .productDto(getDummyProductDto())
                .count(1)
                .build();
    }

    private static List<CartContentDto> getDummyCartContentDtoList() {
        List<CartContentDto> cartContentDtoList = new ArrayList<>();
        cartContentDtoList.add(getDummyCartContentDto());
        return cartContentDtoList;
    }
}

package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.dtos.CartContentDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartCountedProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartProductsDisplayDto;

import java.time.LocalDateTime;
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

    public static CartDto getEmptyCart() {
        return CartDto.builder()
                .cartCreationDateTime(LocalDateTime.now())
                .finalizedCart(false)
                .paidCart(false)
                .cartContentDtoList(new ArrayList<>())
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

    public static CartProductsDisplayDto getCartProductsDisplayDto() {
        CartCountedProductDto cartCountedProductDto = CartCountedProductDto.builder()
                .product(getDummyProductDto())
                .productCount(1)
                .build();
        List<Long> cartProductsIdList = new ArrayList<>();
        cartProductsIdList.add(cartCountedProductDto.getProduct().getId());
        List<CartCountedProductDto> cartCountedProductDtoList = new ArrayList<>();
        cartCountedProductDtoList.add(cartCountedProductDto);
        return CartProductsDisplayDto.builder()
                .cartCountedProductDtoList(cartCountedProductDtoList)
                .cartProductsIdList(cartProductsIdList)
                .build();
    }
}

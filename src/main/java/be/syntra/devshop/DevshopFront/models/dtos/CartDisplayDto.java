package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class CartDisplayDto {
    private Set<CartProductDto> cartProductDtoSet;
    private Set<Long> cartProductsIdSet;
}
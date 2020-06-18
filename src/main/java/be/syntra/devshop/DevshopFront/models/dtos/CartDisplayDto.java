package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Builder
@Getter
public class CartDisplayDto {
    private List<CartProductDto> cartProductDtoList;
    private Set<Long> cartProductsIdSet;
}

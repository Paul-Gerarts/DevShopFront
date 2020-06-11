package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CartProductsDto {
    private List<CartCountedProductDto> cartCountedProductDtoList;
    private List<Long> cartProductsIdList;
}

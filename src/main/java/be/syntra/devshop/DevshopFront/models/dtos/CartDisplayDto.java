package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CartDisplayDto {
    private List<CartDisplayProductDto> cartDisplayProductDtoList;
    private List<Long> cartProductsIdList;
}

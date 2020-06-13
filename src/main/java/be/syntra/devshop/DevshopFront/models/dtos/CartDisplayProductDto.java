package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class CartDisplayProductDto {
    private ProductDto product;
    private Integer productCount;
}

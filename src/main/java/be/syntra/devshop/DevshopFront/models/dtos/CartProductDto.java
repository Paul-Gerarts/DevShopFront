package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDto {
    private ProductDto productDto;
    private Integer count;
}

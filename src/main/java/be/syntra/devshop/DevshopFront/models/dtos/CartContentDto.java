package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CartContentDto {
    /*private Long id;
    private Long productId;*/
    private ProductDto productDto;
    private Integer count;
}

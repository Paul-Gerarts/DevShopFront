package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDtoList {

    private List<ProductDto> products;
}

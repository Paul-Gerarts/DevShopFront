package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductsDisplayListDto {

    private List<ProductDto> products;
    private boolean hasNext;
    private boolean hasPrevious;
    private int currentPage;
    private int totalPages;
}

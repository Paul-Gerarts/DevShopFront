package be.syntra.devshop.DevshopFront.models.dtos;

import be.syntra.devshop.DevshopFront.models.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductListAndMinMaxPrice {
    private List<Product> products;
    private BigDecimal searchResultMinPrice;
    private BigDecimal searchResultMaxPrice;
}

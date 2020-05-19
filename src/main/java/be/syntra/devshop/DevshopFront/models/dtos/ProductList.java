package be.syntra.devshop.DevshopFront.models.dtos;

import be.syntra.devshop.DevshopFront.models.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductList {
    private List<Product> products;
    private BigDecimal searchResultMinPrice;
    private BigDecimal searchResultMaxPrice;
    boolean searchFailure;
}

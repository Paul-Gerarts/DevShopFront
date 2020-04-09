package be.syntra.devshop.DevshopFront.models.dto;

import be.syntra.devshop.DevshopFront.models.Product;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductList {

    private List<Product> products;
}

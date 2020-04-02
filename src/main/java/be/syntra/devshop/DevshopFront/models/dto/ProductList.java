package be.syntra.devshop.DevshopFront.models.dto;

import be.syntra.devshop.DevshopFront.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ProductList {

    private List<Product> productList;
}

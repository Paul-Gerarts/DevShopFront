package be.syntra.devshop.DevshopFront.models.dto;

import be.syntra.devshop.DevshopFront.models.Product;
import lombok.*;

import javax.persistence.Transient;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductList {

    @Transient
    private List<Product> products;
}

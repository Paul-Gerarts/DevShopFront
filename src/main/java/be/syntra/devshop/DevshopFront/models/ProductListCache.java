package be.syntra.devshop.DevshopFront.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProductListCache {

    List<Product> products = new ArrayList<>();
}

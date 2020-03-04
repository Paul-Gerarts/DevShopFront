package be.syntra.devshop.DevshopFront.service;

import be.syntra.devshop.DevshopFront.model.dto.ProductDto;

public interface ProductService {

    /***
     * creates a Dto of a Product obj
     * to use when creating a new Product
     * @return
     */
    ProductDto createEmptyProduct();
}

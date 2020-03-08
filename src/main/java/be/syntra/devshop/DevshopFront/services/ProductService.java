package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SaveStatus;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;

public interface ProductService {

    /***
     * creates a Dto of a Product obj
     * to use in the form when creating a new Product
     * @return
     */
    ProductDto createEmptyProduct();

    /***
     * to send the new ProductDto to a RESTfull service
     * and return a status that corresponds to the success of the action
     * @param productDto
     */

    SaveStatus addProduct(ProductDto productDto);
}

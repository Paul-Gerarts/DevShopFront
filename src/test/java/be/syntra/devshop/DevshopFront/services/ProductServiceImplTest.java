package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceImplTest {

    @Test
    void createEmptyProduct() {
        ProductService productService = new ProductServiceImpl();
        ProductDto emptyProductDto = new ProductDto();

        assertEquals(productService.createEmptyProduct(), emptyProductDto);
    }

}
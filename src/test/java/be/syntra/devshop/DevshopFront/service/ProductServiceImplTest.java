package be.syntra.devshop.DevshopFront.service;

import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceImplTest {

    @Test
    void createEmptyProduct() {
        ProductService productService = new ProductServiceImpl();
        ProductDto emptyProductDto = new ProductDto();

        assertEquals(productService.createEmptyProduct(), emptyProductDto);
    }

    @Test
    void addProductSuccess() {
    }
}
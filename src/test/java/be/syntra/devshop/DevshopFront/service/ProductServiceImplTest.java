package be.syntra.devshop.DevshopFront.service;

import be.syntra.devshop.DevshopFront.client.ProductClient;
import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceImplTest {

    @Mock
    ProductClient productClient;

    @Test
    void createEmptyProduct() {
        ProductService productService = new ProductServiceImpl(/*productClient*/);
        ProductDto emptyProductDto = new ProductDto();

        assertEquals(productService.createEmptyProduct(), emptyProductDto);
    }
}
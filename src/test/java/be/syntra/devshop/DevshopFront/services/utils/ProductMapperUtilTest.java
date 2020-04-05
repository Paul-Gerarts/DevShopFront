package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import org.junit.jupiter.api.Test;

import static be.syntra.devshop.DevshopFront.TestUtils.ProductUtils.getDummyProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperUtilTest {

    @Test
    void convertToProductDtoTest() {
        // given
        Product product = getDummyProduct();

        // when
        ProductDto mappedProductDto = ProductMapperUtil.convertToProductDto(product);

        // then
        assertEquals(mappedProductDto.getClass(), ProductDto.class);
        assertEquals(mappedProductDto.getName(), product.getName());
        assertEquals(mappedProductDto.getPrice(), product.getPrice());
        assertEquals(mappedProductDto.getDescription(), product.getDescription());
        assertEquals(mappedProductDto.isArchived(), product.isArchived());
    }
}
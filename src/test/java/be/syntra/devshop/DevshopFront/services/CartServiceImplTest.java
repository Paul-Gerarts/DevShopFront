package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(CartServiceImpl.class)
class CartServiceImplTest {
    @Autowired
    CartDto currentCart;

    @Autowired
    CartServiceImpl cartService;

    @Test
    void addToNewCart() {
        // given
        Product product = ProductUtils.getDummyProduct();

        // when
        cartService.addToCart(product);

        // then
        assertEquals(1, currentCart.getProducts().size());
    }

    @Test
    void addToExistingCart() {
        // given
        Product product = ProductUtils.getDummyProduct();

        // when
        cartService.addToCart(product);

        // then
        assertEquals(2, currentCart.getProducts().size());
    }
}
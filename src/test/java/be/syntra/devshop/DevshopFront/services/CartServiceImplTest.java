package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.TestUtils.CartUtils;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartServiceImplTest {

    CartService cartService = new CartServiceImpl();

    @Test
    public void getNewCartTest() {
        // given

        // when
        CartDto newCart = cartService.getCart();

        // then
        assertEquals(newCart.getProducts(), null);
        assertEquals(newCart.isActiveCart(), true);
        assertEquals(newCart.isFinalizedCart(), false);
        assertEquals(newCart.isPaidCart(), false);
    }

    @Test
    public void getExistingCartTest() {
        // given
        CartDto currentCart = CartUtils.getCartWithOneDummyProduct();

        // when
        CartDto getCart = cartService.getCart();

        // then
        assertEquals(getCart.getProducts().size(), 1);
        assertEquals(getCart.isActiveCart(), true);
        assertEquals(getCart.isFinalizedCart(), false);
        assertEquals(getCart.isPaidCart(), false);
    }
}
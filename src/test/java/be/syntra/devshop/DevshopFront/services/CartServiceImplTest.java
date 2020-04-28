package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.testutils.CartUtils;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.ProductUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RestClientTest(CartService.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CartServiceImplTest {

    @Autowired
    CartDto currentCart;

    @Autowired
    CartServiceImpl cartService;

    @Test
    public void getNewCartTest() {
        // given
        // when
        CartDto newCart = cartService.getCart();

        // then
        assertEquals(true,newCart.isActiveCart());
        assertEquals(false,newCart.isFinalizedCart());
        assertEquals(false,newCart.isPaidCart());
        assertEquals(0,newCart.getProducts().size());
    }

    @Test
    public void getExistingCartTest() {
        // given
        cartService.addToCart(ProductUtils.getDummyNonArchivedProduct());

        // when
        CartDto getCart = cartService.getCart();

        // then
        assertEquals(getCart.getProducts().size(), 1);
        assertEquals(getCart.isActiveCart(), true);
        assertEquals(getCart.isFinalizedCart(), false);
        assertEquals(getCart.isPaidCart(), false);
    }

    @Test
    public void payCartTest() {
        //given
        CartDto cartDto = CartUtils.getCartWithOneDummyProduct();
        String username = "name";

    }
}

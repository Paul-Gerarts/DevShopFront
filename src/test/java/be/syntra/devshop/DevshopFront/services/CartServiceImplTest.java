package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.services.utils.CartUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CartServiceImplTest {

    @Autowired
    CartDto currentCart;


    @InjectMocks
    CartServiceImpl cartService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore
    public void getNewCartTest() {
        // given
        // when
        CartDto newCart = cartService.getCart();

        // then
        assertEquals(newCart.isActiveCart(), true);
        assertEquals(newCart.isFinalizedCart(), false);
        assertEquals(newCart.isPaidCart(), false);
        assertEquals(newCart.getProducts().size(), 0);
    }

    @Test
    @Ignore
    public void getExistingCartTest() {
        // given
        CartDto currentCart = CartUtils.getCartWithOneDummyProduct();

        // when
        cartService.addToCart(ProductUtils.getDummyNonArchivedProduct());
        CartDto getCart = cartService.getCart();

        // then
        assertEquals(getCart.getProducts().size(), 1);
        assertEquals(getCart.isActiveCart(), true);
        assertEquals(getCart.isFinalizedCart(), false);
        assertEquals(getCart.isPaidCart(), false);
    }
}

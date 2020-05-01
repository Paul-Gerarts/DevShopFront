package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.testutils.*;
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
    void canAddOneToProductInCartTest() {
        // given
        cartService.addToCart(ProductUtils.getDummyNonArchivedProduct());

        // when
        cartService.addOneToProductInCart(1L);

        // then
        assertEquals(currentCart.getProducts().get(0).getTotalInCart(), 2);
    }

    @Test
    void canRemoveOneFromProductInCartTest() {
        // given
        cartService.addToCart(ProductUtils.getDummyNonArchivedProduct());
        currentCart.getProducts().get(0).setTotalInCart(3);

        // when
        cartService.removeOneFromProductInCart(1L);

        // then
        assertEquals(currentCart.getProducts().get(0).getTotalInCart(), 2);
    }

    @Test
    void canRemoveProductFromCartTest() {
        // given
        cartService.addToCart(ProductUtils.getDummyNonArchivedProduct());

        // when
        cartService.removeProductFromCart(1L);

        // then
        assertEquals(currentCart.getProducts().size(), 0);
    }

    @Test
    void willRemoveProductIfNumberInCartIsZeroWhenRemovingOneTest() {
        // given
        cartService.addToCart(ProductUtils.getDummyNonArchivedProduct());
        currentCart.getProducts().get(0).setTotalInCart(1);

        // when
        cartService.removeOneFromProductInCart(1L);

        // then
        assertEquals(currentCart.getProducts().size(), 0);
    }

    @Test
    void payCartTest() {
        //given
        CartDto cartDto = CartUtils.getCartWithOneDummyProduct();


    }

    @Test
    void getTotalCartPriceTest() {
        //given
        CartDto cartDto = CartUtils.getCartWithMultipleNonArchivedProducts();
        PaymentDto paymentDto = PaymentUtils.createPaymentDtoWithTotalCartPrice();
        currentCart.setProducts(cartDto.getProducts());

        //when
        cartService.cartTotalPrice(paymentDto);

        //then
        assertEquals("196", paymentDto.getTotalCartPrice().toString());
    }
}

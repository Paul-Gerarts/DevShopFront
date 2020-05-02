package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.testutils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(CartService.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CartServiceImplTest {
    @Autowired
    RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${cartEndpoint}")
    private String endpoint;

    @Mock
    private Principal principal;

    @Autowired
    CartDto currentCart;

    @Autowired
    CartServiceImpl cartService;

    @Autowired
    private JsonUtils jsonUtils;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

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
        currentCart = CartUtils.getCartWithOneDummyProduct();
        final String cartDtoAsJson = jsonUtils.asJsonString(currentCart);
        final String expectedEndpoint = baseUrl + endpoint;
        currentCart.setUser(principal.getName());

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(cartDtoAsJson));

        // when
        StatusNotification statusNotification = cartService.payCart(principal.getName());

        // then
        mockServer.verify();
        assertEquals(StatusNotification.SUCCESS, statusNotification);
    }

    @Test
    void getTotalCartPriceTest() {
        //given
        CartDto cartDto = CartUtils.getCartWithMultipleNonArchivedProducts();
        currentCart = cartDto;
        PaymentDto paymentDto = PaymentUtils.createPaymentDtoWithTotalCartPrice();
        paymentDto.setTotalCartPrice(cartService.getCartTotalPrice());
        currentCart.setProducts(cartDto.getProducts());

        //then
        assertEquals(cartService.getCartTotalPrice(), paymentDto.getTotalCartPrice());
    }

    @Test
    void setCartToFinalizedTest() {
        //given
        CartDto cartDto = CartUtils.getCartWithOneDummyProduct();

        //when
        cartDto.setActiveCart(false);
        cartDto.setFinalizedCart(true);
        cartDto.setPaidCart(false);

        //then
        assertTrue(cartDto.isFinalizedCart());
        assertFalse(cartDto.isActiveCart());
        assertFalse(cartDto.isPaidCart());
    }
}

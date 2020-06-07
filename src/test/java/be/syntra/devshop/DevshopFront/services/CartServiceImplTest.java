package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.testutils.CartUtils;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
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

import java.math.BigDecimal;
import java.security.Principal;

import static be.syntra.devshop.DevshopFront.testutils.CartUtils.getCartWithMultipleNonArchivedProducts;
import static be.syntra.devshop.DevshopFront.testutils.PaymentUtils.createPaymentDtoWithTotalCartPrice;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getOtherDummyProductDto;
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
        assertEquals(false,newCart.isFinalizedCart());
        assertEquals(false,newCart.isPaidCart());
        assertEquals(0, newCart.getProductDtos().size());
    }

    @Test
    public void getExistingCartTest() {
        // given
        ProductDto dummyProductDto = getOtherDummyProductDto();

        //cartService.addToCart(ProductUtils.getDummyNonArchivedProduct());
        cartService.addToCart(dummyProductDto.getId());

        // when
        CartDto getCart = cartService.getCart();

        // then
        assertEquals(getCart.getProductDtos().size(), 1);
        assertEquals(getCart.isFinalizedCart(), false);
        assertEquals(getCart.isPaidCart(), false);
    }

    @Test
    void canAddOneToProductInCartTest() {
        // given
        ProductDto dummyProductDto = getOtherDummyProductDto();
        cartService.addToCart(dummyProductDto.getId());

        // when
        cartService.addOneToProductInCart(1L);

        // then
        assertEquals(currentCart.getProductDtos().get(0).getTotalInCart(), 3);
    }

    @Test
    void canRemoveOneFromProductInCartTest() {
        // given
        ProductDto dummyProductDto = getOtherDummyProductDto();
        cartService.addToCart(dummyProductDto.getId());
        currentCart.getProductDtos().get(0).setTotalInCart(3);

        // when
        cartService.removeOneFromProductInCart(1L);

        // then
        assertEquals(currentCart.getProductDtos().get(0).getTotalInCart(), 2);
    }

    @Test
    void canRemoveProductFromCartTest() {
        // given
        ProductDto dummyProductDto = getOtherDummyProductDto();
        cartService.addToCart(dummyProductDto.getId());

        // when
        cartService.removeProductFromCart(1L);

        // then
        assertEquals(currentCart.getProductDtos().size(), 0);
    }

    @Test
    void willRemoveProductIfNumberInCartIsZeroWhenRemovingOneTest() {
        // given
        ProductDto dummyProductDto = getOtherDummyProductDto();
        cartService.addToCart(dummyProductDto.getId());
        currentCart.getProductDtos().get(0).setTotalInCart(1);

        // when
        cartService.removeOneFromProductInCart(1L);

        // then
        assertEquals(currentCart.getProductDtos().size(), 0);
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
        currentCart = getCartWithMultipleNonArchivedProducts();
        PaymentDto paymentDto = createPaymentDtoWithTotalCartPrice();

        //when
        BigDecimal result = cartService.getCartTotalPrice(currentCart);

        //then
        assertEquals(result, paymentDto.getTotalCartPrice());
    }

    @Test
    void setCartToFinalizedTest() {
        //given
        CartDto cartDto = CartUtils.getCartWithOneDummyProduct();

        //when
        cartDto.setFinalizedCart(true);
        cartDto.setPaidCart(false);

        //then
        assertTrue(cartDto.isFinalizedCart());
        assertFalse(cartDto.isPaidCart());
    }
}

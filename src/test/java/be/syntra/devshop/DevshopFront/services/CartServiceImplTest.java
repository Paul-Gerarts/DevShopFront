package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartProductsDisplayDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import be.syntra.devshop.DevshopFront.testutils.CartUtils;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import be.syntra.devshop.DevshopFront.testutils.WebContextTestExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(CartService.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class, WebContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
/*@TestExecutionListeners({WebContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})*/
class CartServiceImplTest {

    @Autowired
    RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${cartEndpoint}")
    private String endpoint;

    @Mock
    private Principal principal;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getNewCartTest() {
        // when
        CartDto newCart = cartService.getCart();

        // then
        assertEquals(false,newCart.isFinalizedCart());
        assertEquals(false,newCart.isPaidCart());
        assertEquals(0, newCart.getCartContentDtoList().size(), 0);
    }

    @Test
    public void getExistingCartTest() {
        // given
        ProductDto dummyProductDto = getDummyProductDto();
        Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(anyLong())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(any(Product.class))).thenReturn(dummyProductDto);
        cartService.addToCart(dummyProductDto.getId());

        // when
        CartDto getCart = cartService.getCart();

        // then
        assertEquals(getCart.getCartContentDtoList().size(), 1);
        assertEquals(getCart.isFinalizedCart(), false);
        assertEquals(getCart.isPaidCart(), false);
    }

    @Test
    void canAddOneToProductInCartTest() {
        // given
        final ProductDto dummyProductDto = getOtherDummyProductDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProductDto.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        cartService.addToCart(dummyProductDto.getId());

        // when
        cartService.addOneToProductInCart(dummyProduct.getId());

        // then
        //assertEquals(currentCart.getProductDtos().get(0).getTotalInCart(), 3);
        assertEquals(currentCart.getCartContentDtoList().get(0).getCount(), 2);
    }

    @Test
    void canRemoveOneFromProductInCartTest() {
        // given
        final ProductDto dummyProductDto = getOtherDummyProductDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProductDto.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        cartService.addToCart(dummyProductDto.getId());
        //currentCart.getProductDtos().get(0).setTotalInCart(3);
        currentCart.getCartContentDtoList().get(0).setCount(3);

        // when
        cartService.removeOneFromProductInCart(1L);

        // then
        //assertEquals(currentCart.getProductDtos().get(0).getTotalInCart(), 2);
        assertEquals(currentCart.getCartContentDtoList().get(0).getCount(), 2);
    }

    @Test
    void canRemoveProductFromCartTest() {
        // given
        final ProductDto dummyProductDto = getOtherDummyProductDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProductDto.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        cartService.addToCart(dummyProductDto.getId());

        // when
        cartService.removeProductFromCart(1L);

        // then
        assertEquals(currentCart.getCartContentDtoList().size(), 0);
    }

    @Test
    void willRemoveProductIfNumberInCartIsZeroWhenRemovingOneTest() {
        // given
        final ProductDto dummyProductDto = getOtherDummyProductDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProductDto.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        cartService.addToCart(dummyProductDto.getId());

        // when
        cartService.removeOneFromProductInCart(1L);

        // then
        assertEquals(currentCart.getCartContentDtoList().size(), 0);
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

    @Test
    void canAddProductToCartTest() {
        // given
        final ProductDto dummyProductDto = getOtherDummyProductDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProductDto.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);

        // when
        cartService.addToCart(dummyProduct.getId());

        // then
        assertEquals(currentCart.getCartContentDtoList().get(0).getCount(), 1);
        assertEquals(currentCart.getCartContentDtoList().size(), 1);
    }

    @Test
    void canAddSameProductToCartTest() {
        // given
        final ProductDto dummyProductDto = getOtherDummyProductDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProductDto.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        cartService.addToCart(dummyProduct.getId());

        // when
        cartService.addToCart(dummyProduct.getId());

        // then
        assertEquals(currentCart.getCartContentDtoList().get(0).getCount(), 2);
        assertEquals(currentCart.getCartContentDtoList().size(), 1);
    }

    @Test
    void canGetCartProductsDto() {
        // given
        final ProductDto dummyProductDto = getOtherDummyProductDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProductDto.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        cartService.addToCart(dummyProduct.getId());

        // when
        final CartProductsDisplayDto cartProductsDto = cartService.getCartProductsDto();

        // then
        assertEquals(cartProductsDto.getCartCountedProductDtoList().size(), 1);
        assertEquals(cartProductsDto.getCartProductsIdList().get(0), dummyProduct.getId());
    }
}

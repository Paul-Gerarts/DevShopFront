package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.*;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private CartDto currentCart;
    private ProductService productService;
    private ProductMapper productMapper;

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    private CartServiceImpl(
            CartDto currentCart,
            ProductService productService,
            ProductMapper productMapper
    ) {
        this.currentCart = currentCart;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cartEndpoint}")
    private String endpoint;

    private String resourceUrl = null;

    @PostConstruct
    private void init() {
        resourceUrl = baseUrl.concat(endpoint);
    }

    @Override
    public CartDto getCart() {
        return currentCart;
    }

    @Override
    public void addToCart(Long id) {
        List<CartProductDto> contentDtoList = getCart().getCartProductDtoList();
        if (productNotInCart(id)) {
            contentDtoList.add(CartProductDto.builder()
                    .productDto(productMapper.convertToProductDto(productService.findById(id)))
                    .count(1)
                    .build());
        } else {
            CartProductDto currentCartProductDto = contentDtoList.stream()
                    .filter(cartProductDto -> cartProductDto.getProductDto().getId().equals(id))
                    .findFirst()
                    .get();
            currentCartProductDto.setCount(currentCartProductDto.getCount() + 1);
        }
    }

    private boolean productNotInCart(Long id) {
        return getCart().getCartProductDtoList().stream()
                .map(CartProductDto::getProductDto)
                .map(ProductDto::getId)
                .filter(i -> i.equals(id))
                .findFirst()
                .isEmpty();
    }

    @Override
    public void addOneToProductInCart(Long productId) {
        CartProductDto cartProductDto = getCartContentDtoFromCart(productId);
        cartProductDto.setCount(cartProductDto.getCount() + 1);
    }

    private CartProductDto getCartContentDtoFromCart(Long productId) {
        return getCart().getCartProductDtoList().stream()
                .filter(cartCountedProductDto -> productId.equals(cartCountedProductDto.getProductDto().getId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + " was not found in your cart"));
    }

    @Override
    public void removeOneFromProductInCart(Long productId) {
        CartProductDto cartProductDto = getCartContentDtoFromCart(productId);
        if (cartProductDto.getCount() == 1) {
            removeProductFromCart(productId);
        } else {
            cartProductDto.setCount(cartProductDto.getCount() - 1);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        CartProductDto cartProductDto = getCartContentDtoFromCart(productId);
        getCart().getCartProductDtoList().remove(cartProductDto);
    }

    @Override
    public StatusNotification payCart(String userName) {
        currentCart.setUser(userName);
        setCartToFinalized(currentCart);

        ResponseEntity<CartDto> cartDtoResponseEntity = restTemplate.postForEntity(resourceUrl, wrap(currentCart), CartDto.class);
        if (HttpStatus.CREATED.equals(cartDtoResponseEntity.getStatusCode())) {
            log.info("payCart() -> successful {}", currentCart);
            currentCart.getCartProductDtoList().clear();
            return StatusNotification.SUCCESS;
        }
        return StatusNotification.PAYMENT_FAIL;
    }

    /*
     * when using the raw currentCart, a stackOverFlowError is thrown
     * instead, we're wrapping our CartDto to be able to persist without a problem
     * @Return: CartDto which is a copy of the currentCart
     */
    private CartDto wrap(CartDto currentCart) {
        return CartDto.builder()
                .finalizedCart(currentCart.isFinalizedCart())
                .paidCart(currentCart.isPaidCart())
                .cartCreationDateTime(currentCart.getCartCreationDateTime())
                .user(currentCart.getUser())
                .cartProductDtoList(currentCart.getCartProductDtoList())
                .build();
    }

    private void setCartToFinalized(CartDto cartDto) {
        cartDto.setFinalizedCart(true);
        cartDto.setPaidCart(false);
    }

    @Override
    public BigDecimal getCartTotalPrice(CartDto currentCart) {
        return currentCart.getCartProductDtoList().stream()
                .map(cartProductDto -> {
                    BigDecimal productPrice = cartProductDto.getProductDto().getPrice();
                    int count = cartProductDto.getCount();
                    return productPrice.multiply(new BigDecimal(count));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartDisplayDto getCartDisplayDto() {
        return CartDisplayDto.builder().cartDisplayProductDtoList(
                getCart().getCartProductDtoList().stream()
                        .map(this::createCartCountedProduct)
                        .collect(Collectors.toList()))
                .cartProductsIdList(
                        getCart().getCartProductDtoList().stream()
                                .map(CartProductDto::getProductDto)
                                .map(ProductDto::getId)
                                .collect(Collectors.toList()))
                .build();
    }

    private CartDisplayProductDto createCartCountedProduct(CartProductDto cartProductDto) {
        return CartDisplayProductDto.builder()
                .product(productMapper.convertToProductDto(productService.findById(cartProductDto.getProductDto().getId())))
                .productCount(cartProductDto.getCount())
                .build();
    }
}
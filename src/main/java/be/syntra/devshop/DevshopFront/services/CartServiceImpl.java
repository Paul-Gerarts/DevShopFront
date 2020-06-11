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
        List<CartContentDto> contentDtoList = getCart().getCartContentDtoList();
        if (productNotInCart(id)) {
            contentDtoList.add(CartContentDto.builder()
                    .productDto(productMapper.convertToProductDto(productService.findById(id)))
                    .count(1)
                    .build());
        } else {
            CartContentDto currentCartContentDto = contentDtoList.stream()
                    .filter(cartContentDto -> cartContentDto.getProductDto().getId().equals(id))
                    .findFirst()
                    .get();
            currentCartContentDto.setCount(currentCartContentDto.getCount() + 1);
        }
    }

    private boolean productNotInCart(Long id) {
        return getCart().getCartContentDtoList().stream()
                .map(CartContentDto::getProductDto)
                .map(ProductDto::getId)
                .filter(i -> i.equals(id))
                .findFirst()
                .isEmpty();
    }

    @Override
    public void addOneToProductInCart(Long productId) {
        CartContentDto cartContentDto = getCartContentDtoFromCart(productId);
        cartContentDto.setCount(cartContentDto.getCount() + 1);
    }

    private CartContentDto getCartContentDtoFromCart(Long productId) {
        return getCart().getCartContentDtoList().stream()
                .filter(cartCountedProductDto -> productId.equals(cartCountedProductDto.getProductDto().getId()))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + " was not found in your cart"));
    }

    @Override
    public void removeOneFromProductInCart(Long productId) {
        CartContentDto cartContentDto = getCartContentDtoFromCart(productId);
        if (cartContentDto.getCount() == 1) {
            removeProductFromCart(productId);
        } else {
            cartContentDto.setCount(cartContentDto.getCount() - 1);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        CartContentDto cartContentDto = getCartContentDtoFromCart(productId);
        getCart().getCartContentDtoList().remove(cartContentDto);
    }

    @Override
    public StatusNotification payCart(String userName) {
        currentCart.setUser(userName);
        setCartToFinalized(currentCart);

        ResponseEntity<CartDto> cartDtoResponseEntity = restTemplate.postForEntity(resourceUrl, wrap(currentCart), CartDto.class);
        if (HttpStatus.CREATED.equals(cartDtoResponseEntity.getStatusCode())) {
            log.info("payCart() -> successful {}", currentCart);
            currentCart.getCartContentDtoList().clear();
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
                .cartContentDtoList(currentCart.getCartContentDtoList())
                .build();
    }

    private void setCartToFinalized(CartDto cartDto) {
        cartDto.setFinalizedCart(true);
        cartDto.setPaidCart(false);
    }

    @Override
    public BigDecimal getCartTotalPrice(CartDto currentCart) {
        return getCart().getCartContentDtoList().stream()
                .map(cartContentDto -> {
                    BigDecimal productPrice = cartContentDto.getProductDto().getPrice();
                    int count = cartContentDto.getCount();
                    return productPrice.multiply(new BigDecimal(count));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartProductsDisplayDto getCartProductsDto() {
        return CartProductsDisplayDto.builder().cartCountedProductDtoList(
                getCart().getCartContentDtoList().stream()
                        .map(this::createCartCountedProduct)
                        .collect(Collectors.toList()))
                .cartProductsIdList(
                        getCart().getCartContentDtoList().stream()
                                .map(CartContentDto::getProductDto)
                                .map(ProductDto::getId)
                                .collect(Collectors.toList()))
                .build();
    }

    private CartCountedProductDto createCartCountedProduct(CartContentDto cartContentDto) {
        return CartCountedProductDto.builder()
                .product(productMapper.convertToProductDto(productService.findById(cartContentDto.getProductDto().getId())))
                .productCount(cartContentDto.getCount())
                .build();
    }
}
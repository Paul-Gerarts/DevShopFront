package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDisplayDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
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
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final CartDto currentCart;
    private final ProductService productService;
    private final ProductMapper productMapper;

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
        Set contentDtoList = getCart().getCartProductDtoSet();
        CartProductDto currentCartProductDto = getCartContentDtoFromCart(id)
                .orElse(CartProductDto.builder()
                        .productDto(productMapper.convertToProductDto(productService.findById(id)))
                        .count(0)
                        .build());
        currentCartProductDto.setCount(currentCartProductDto.getCount() + 1);
        contentDtoList.add(currentCartProductDto);
    }

    private Optional<CartProductDto> getCartContentDtoFromCart(Long productId) {
        return getCart().getCartProductDtoSet().stream()
                .filter(cartCountedProductDto -> productId.equals(cartCountedProductDto.getProductDto().getId()))
                .findFirst();
    }

    private Supplier<ProductNotFoundException> productNotFoundExceptionSupplier(Long productId){
        return () -> new ProductNotFoundException("Product with id = " + productId + " was not found in your cart");
    }

    @Override
    public void removeOneFromProductInCart(Long productId) {
        CartProductDto cartProductDto = getCartContentDtoFromCart(productId).orElseThrow(productNotFoundExceptionSupplier(productId));
        if (cartProductDto.getCount() == 1) {
            removeProductFromCart(productId);
        } else {
            cartProductDto.setCount(cartProductDto.getCount() - 1);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        CartProductDto cartProductDto = getCartContentDtoFromCart(productId).orElseThrow(productNotFoundExceptionSupplier(productId));
        getCart().getCartProductDtoSet().remove(cartProductDto);
    }

    @Override
    public StatusNotification payCart(String userName) {
        currentCart.setUser(userName);
        currentCart.setFinalizedCart(true);
        ResponseEntity<CartDto> cartDtoResponseEntity = restTemplate.postForEntity(resourceUrl, wrap(currentCart), CartDto.class);
        if (HttpStatus.CREATED.equals(cartDtoResponseEntity.getStatusCode())) {
            log.info("payCart() -> successful {}", currentCart);
            currentCart.getCartProductDtoSet().clear();
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
                .cartProductDtoSet(currentCart.getCartProductDtoSet())
                .build();
    }

    @Override
    public BigDecimal getCartTotalPrice(CartDto currentCart) {
        return currentCart.getCartProductDtoSet().stream()
                .map(cartProductDto -> cartProductDto.getProductDto()
                        .getPrice()
                        .multiply(new BigDecimal(cartProductDto.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartDisplayDto getCartDisplayDto() {
        return CartDisplayDto.builder()
                .cartProductDtoSet(getProducts())
                .cartProductsIdSet(getProductIds())
                .build();
    }

    private Set<Long> getProductIds() {
        return getCart().getCartProductDtoSet().stream()
                .map(CartProductDto::getProductDto)
                .map(ProductDto::getId)
                .collect(Collectors.toSet());
    }

    private Set<CartProductDto> getProducts() {
        return getCart().getCartProductDtoSet().stream()
                .map(this::createCartProduct)
                .collect(Collectors.toSet());
    }

    private CartProductDto createCartProduct(CartProductDto cartProductDto) {
        return CartProductDto.builder()
                .productDto(productMapper.convertToProductDto(productService.findById(cartProductDto.getProductDto().getId())))
                .count(cartProductDto.getCount())
                .build();
    }
}
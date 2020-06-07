package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.*;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        /*//product.setTotalInCart(product.getTotalInCart() + 1);
        productDto.setTotalInCart(productDto.getTotalInCart() + 1);
        //List<Product> productList = getCart().getProducts();
        List<ProductDto> productList = getCart().getProductDtos();
        productList.add(productDto);
        getCart().setProductDtos(productList);*/

        ProductDto productDto = productMapper.convertToProductDto(productService.findById(id));
        List<CartContentDto> contentDtoList = getCart().getCartContentDtoList();
        if (null != contentDtoList) {
            List<CartContentDto> list = new ArrayList<>();
            list.add(CartContentDto.builder().productId(id).count(1).build());
            getCart().setCartContentDtoList(list);
        } else {
            Optional<CartContentDto> cartContentDtoOptional =
                    contentDtoList.stream()
                            .filter(content -> content.getId().equals(productDto.getId()))
                            .findFirst();
            CartContentDto updatedCartContentDto;
            if (cartContentDtoOptional.isPresent()) {
                updatedCartContentDto = cartContentDtoOptional.get();
                updatedCartContentDto.setCount(updatedCartContentDto.getCount() + 1);
            } else {
                updatedCartContentDto = CartContentDto.builder().productId(productDto.getId()).count(1).build();
            }
            contentDtoList.add(updatedCartContentDto);
        }
    }

    @Override
    public void addOneToProductInCart(Long productId) {
        //Product productToAlter = getProductFromCartById(productId);
        ProductDto productToAlter = getProductFromCartById(productId);
        productToAlter.setTotalInCart(productToAlter.getTotalInCart() + 1);
    }

    @Override
    public void removeOneFromProductInCart(Long productId) {
        //Product productToAlter = getProductFromCartById(productId);
        ProductDto productToAlter = getProductFromCartById(productId);
        final int totalInCart = productToAlter.getTotalInCart();
        if (totalInCart == 1) {
            removeProductFromCart(productId);
        } else {
            productToAlter.setTotalInCart(totalInCart - 1);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        //Product productToRemove = getProductFromCartById(productId);
        ProductDto productToRemove = getProductFromCartById(productId);
        productToRemove.setTotalInCart(0);
        currentCart.getProductDtos().remove(productToRemove);
    }

    private ProductDto getProductFromCartById(Long productId) {
        return currentCart.getProductDtos().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + " was not found in your cart"));
    }

    @Override
    public StatusNotification payCart(String userName) {
        currentCart.setUser(userName);
        setCartToFinalized(currentCart);
        CartDto cartDto = wrap(currentCart);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/testCartDto2.json"), cartDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseEntity<CartDto> cartDtoResponseEntity = restTemplate.postForEntity(resourceUrl, cartDto, CartDto.class);
        if (HttpStatus.CREATED.equals(cartDtoResponseEntity.getStatusCode())) {
            log.info("payCart() -> successful {}", currentCart);
            currentCart.getProductDtos().forEach(product -> product.setTotalInCart(0));
            currentCart.getProductDtos().clear();
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
                .productDtos(currentCart.getProductDtos())
                .cartContentDtoList(currentCart.getCartContentDtoList())
                .build();
    }

    private void setCartToFinalized(CartDto cartDto) {
        cartDto.setFinalizedCart(true);
        cartDto.setPaidCart(false);
    }

    @Override
    public BigDecimal getCartTotalPrice(CartDto currentCart) {
        return currentCart.getProductDtos().stream()
                .map(product -> {
                    BigDecimal pricePerProduct = product.getPrice();
                    int totalInCart = product.getTotalInCart();
                    return pricePerProduct.multiply(new BigDecimal(totalInCart));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartProductsDto getCartProductsDto() {
        return CartProductsDto.builder().cartCountedProductDtoList(
                getCart().getCartContentDtoList().stream()
                        .map(cartContentDto -> createCartCountedProduct(cartContentDto))
                        .collect(Collectors.toList())
        ).build();
    }

    private CartCountedProductDto createCartCountedProduct(CartContentDto cartContentDto) {
        return CartCountedProductDto.builder()
                .product(productMapper.convertToProductDto(productService.findById(cartContentDto.getProductId())))
                .productCount(cartContentDto.getCount())
                .build();
    }
}
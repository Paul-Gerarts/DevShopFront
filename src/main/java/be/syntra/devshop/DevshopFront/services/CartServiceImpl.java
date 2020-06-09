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
        /*//product.setTotalInCart(product.getTotalInCart() + 1);
        productDto.setTotalInCart(productDto.getTotalInCart() + 1);
        //List<Product> productList = getCart().getProducts();
        List<ProductDto> productList = getCart().getProductDtos();
        productList.add(productDto);
        getCart().setProductDtos(productList);*/

        ProductDto productDto = productMapper.convertToProductDto(productService.findById(id));
        List<CartContentDto> contentDtoList = getCart().getCartContentDtoList();
        if (productNotInCart(id)) {
            contentDtoList.add(CartContentDto.builder()
                    //.productId(productDto.getId())
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

        /*if (null != contentDtoList) {
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
        }*/
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
        //Product productToAlter = getProductFromCartById(productId);
        /*ProductDto productToAlter = getProductFromCartById(productId);
        productToAlter.setTotalInCart(productToAlter.getTotalInCart() + 1);*/
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
        //Product productToAlter = getProductFromCartById(productId);
        //ProductDto productToAlter = getProductFromCartById(productId);
        /*final int totalInCart = productToAlter.getTotalInCart();
        if (totalInCart == 1) {
            removeProductFromCart(productId);
        } else {
            productToAlter.setTotalInCart(totalInCart - 1);
        }*/
        CartContentDto cartContentDto = getCartContentDtoFromCart(productId);
        if (cartContentDto.getCount() == 1) {
            removeProductFromCart(productId);
        } else {
            cartContentDto.setCount(cartContentDto.getCount() - 1);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        //Product productToRemove = getProductFromCartById(productId);
        /*ProductDto productToRemove = getProductFromCartById(productId);
        productToRemove.setTotalInCart(0);
        currentCart.getProductDtos().remove(productToRemove);*/
        CartContentDto cartContentDto = getCartContentDtoFromCart(productId);
        getCart().getCartContentDtoList().remove(cartContentDto);
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
     /*   CartDto cartDto = wrap(currentCart);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/testCartDto2.json"), cartDto);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        ResponseEntity<CartDto> cartDtoResponseEntity = restTemplate.postForEntity(resourceUrl, wrap(currentCart), CartDto.class);
        if (HttpStatus.CREATED.equals(cartDtoResponseEntity.getStatusCode())) {
            log.info("payCart() -> successful {}", currentCart);
            /*currentCart.getProductDtos().forEach(product -> product.setTotalInCart(0));
            currentCart.getProductDtos().clear();*/
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
        /*return currentCart.getProductDtos().stream()
                .map(product -> {
                    BigDecimal pricePerProduct = product.getPrice();
                    int totalInCart = product.getTotalInCart();
                    return pricePerProduct.multiply(new BigDecimal(totalInCart));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);*/
        return getCart().getCartContentDtoList().stream()
                .map(cartContentDto -> {
                    BigDecimal productPrice = cartContentDto.getProductDto().getPrice();
                    int count = cartContentDto.getCount();
                    return productPrice.multiply(new BigDecimal(count));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartProductsDto getCartProductsDto() {
        return CartProductsDto.builder().cartCountedProductDtoList(
                getCart().getCartContentDtoList().stream()
                        .map(this::createCartCountedProduct)
                        .collect(Collectors.toList())
        ).build();
    }

    private CartCountedProductDto createCartCountedProduct(CartContentDto cartContentDto) {
        return CartCountedProductDto.builder()
                .product(productMapper.convertToProductDto(productService.findById(cartContentDto.getProductDto().getId())))
                .productCount(cartContentDto.getCount())
                .build();
    }
}
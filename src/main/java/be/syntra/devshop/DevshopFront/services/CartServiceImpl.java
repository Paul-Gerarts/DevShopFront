package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
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

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private CartDto currentCart;

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    private CartServiceImpl(CartDto currentCart) {
        this.currentCart = currentCart;
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
    public void addToCart(Product product) {
        product.setTotalInCart(product.getTotalInCart() + 1);
        List<Product> productList = getCart().getProducts();
        productList.add(product);
        getCart().setProducts(productList);
    }

    @Override
    public void addOneToProductInCart(Long productId) {
        Product productToAlter = getProductFromCartById(productId);
        productToAlter.setTotalInCart(productToAlter.getTotalInCart() + 1);
    }

    @Override
    public void removeOneFromProductInCart(Long productId) {
        Product productToAlter = getProductFromCartById(productId);
        final int totalInCart = productToAlter.getTotalInCart();
        if (totalInCart == 1) {
            removeProductFromCart(productId);
        } else {
            productToAlter.setTotalInCart(totalInCart - 1);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        Product productToRemove = getProductFromCartById(productId);
        productToRemove.setTotalInCart(0);
        currentCart.getProducts().remove(productToRemove);
    }

    private Product getProductFromCartById(Long productId) {
        return currentCart.getProducts().stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + productId + " was not found in your cart"));
    }

    @Override
    public StatusNotification payCart(String userName) {
        currentCart.setUser(userName);
        setCartToFinalized(currentCart);
        ResponseEntity<CartDto> cartDtoResponseEntity = restTemplate.postForEntity(resourceUrl, wrap(currentCart), CartDto.class);
        if (HttpStatus.CREATED.equals(cartDtoResponseEntity.getStatusCode())) {
            log.info("payCart() -> successful {}", currentCart);
            currentCart.getProducts().forEach(product -> product.setTotalInCart(0));
            currentCart.getProducts().clear();
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
                .products(currentCart.getProducts())
                .build();
    }

    private void setCartToFinalized(CartDto cartDto) {
        cartDto.setFinalizedCart(true);
        cartDto.setPaidCart(false);
    }

    @Override
    public BigDecimal getCartTotalPrice(CartDto currentCart) {
        return currentCart.getProducts().stream()
                .map(product -> {
                    BigDecimal pricePerProduct = product.getPrice();
                    int totalInCart = product.getTotalInCart();
                    return pricePerProduct.multiply(new BigDecimal(totalInCart));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
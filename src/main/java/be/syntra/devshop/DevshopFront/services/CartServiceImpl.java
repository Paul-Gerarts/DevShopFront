package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.exceptions.UserNotFoundException;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private CartDto currentCart;
    private UserRepository userRepository;

    @Value("${baseUrl}")
    private String baseUrl;

    @Autowired
    private CartServiceImpl(CartDto currentCart, UserRepository userRepository) {
        this.currentCart = currentCart;
        this.userRepository = userRepository;
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
        log.info("addToCart() -> {}", currentCart.getProducts().size());
    }

    @Override
    public void addOneToProductInCart(Long productId) {
        Product productToAlter = getProductFromCartById(productId);
        // todo: DEV-034: update in cachedProducts-replacement
        productToAlter.setTotalInCart(productToAlter.getTotalInCart() + 1);
    }

    @Override
    public void removeOneFromProductInCart(Long productId) {
        Product productToAlter = getProductFromCartById(productId);
        final int totalInCart = productToAlter.getTotalInCart();
        // todo: DEV-034: update in cachedProducts-replacement
        if (totalInCart == 1) {
            removeProductFromCart(productId);
        } else {
            productToAlter.setTotalInCart(totalInCart - 1);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        Product productToRemove = getProductFromCartById(productId);
        // todo: DEV-034: update in cachedProducts-replacement
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
    public StatusNotification payCart(CartDto cartDto, Principal userName) {
        setUserName(cartDto, getUsername(userName));
        log.info("username() -> {}", cartDto.getUser());
        setCartToFinalized(cartDto);
        HttpEntity<CartDto> requestDto = new HttpEntity<>(cartDto);
        log.info("cart() -> {}", cartDto.toString());

        ResponseEntity<CartDto> cartDtoResponseEntity = restTemplate.postForEntity(resourceUrl, requestDto, CartDto.class);
        if (HttpStatus.CREATED.equals(cartDtoResponseEntity.getStatusCode())) {
            log.info("payCart() -> successful {}", cartDto.getClass());
            return StatusNotification.SUCCESS;
        }
        return StatusNotification.PAYMENT_FAIL;
    }

    private String getUsername(Principal user) {
        return userRepository.findByUserName(user.getName())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with name %s could not be found", user.getName())))
                .getUsername();
    }

    private void setUserName(CartDto cartDto, String user) {
        cartDto.setUser(user);
    }

    private void setCartToFinalized(CartDto cartDto) {
        cartDto.setActiveCart(false);
        cartDto.setFinalizedCart(true);
        cartDto.setPaidCart(false);
    }

    @Override
    public void cartTotalPrice(PaymentDto paymentDto) {
        paymentDto.setTotalCartPrice(getCart().getProducts().stream()
                .map(product -> {
                    BigDecimal pricePerProduct = product.getPrice();
                    int totalInCart = product.getTotalInCart();
                    return pricePerProduct.multiply(new BigDecimal(totalInCart));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
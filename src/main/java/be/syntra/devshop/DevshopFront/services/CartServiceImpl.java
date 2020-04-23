package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public StatusNotification payCart(CartDto cartDto) {
        // cartDto is send to backend through rest template url with the users name which is the email.
        return StatusNotification.SUCCESS;
    }

    @Override
    public void cartTotalPrice(PaymentDto paymentDto) {
        paymentDto.setTotalPrice(getCart().getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @Override
    public void addOneToProductInCart(Long productId) {
        Product productToAlter = currentCart.getProducts().stream().filter(product -> product.getId() == productId).findFirst().get();
        // todo: DEV-034: update in cachedProducts-replacement
        productToAlter.setTotalInCart(productToAlter.getTotalInCart() + 1);
    }

    @Override
    public void removeOneFromProductInCart(Long productId) {
        Product productToAlter = currentCart.getProducts().stream().filter(product -> product.getId() == productId).findFirst().get();
        // todo: DEV-034: update in cachedProducts-replacement
        productToAlter.setTotalInCart(productToAlter.getTotalInCart() - 1);
        if (productToAlter.getTotalInCart() == 0) {
            // todo: DEV-034: update in cachedProducts-replacement
            removeProductFromCart(productId);
        }
    }

    @Override
    public void removeProductFromCart(Long productId) {
        Product productToRemove = currentCart.getProducts().stream().filter(product -> product.getId() == productId).findFirst().get();
        // todo: DEV-034: update in cachedProducts-replacement
        productToRemove.setTotalInCart(0);
        currentCart.getProducts().remove(productToRemove);
    }
}
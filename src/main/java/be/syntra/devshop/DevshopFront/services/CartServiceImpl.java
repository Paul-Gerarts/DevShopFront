package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        productToAlter.setTotalInCart(totalInCart - 1);
        if (totalInCart == 1) {
            // todo: DEV-034: update in cachedProducts-replacement
            removeProductFromCart(productId);
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
}
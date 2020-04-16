package be.syntra.devshop.DevshopFront.services;

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
    @Value("${cartEndpointEnding}")
    private String endpointEnding;

    @Autowired
    private CartServiceImpl(CartDto currentCart) {
        this.currentCart = currentCart;
    }

    @Autowired
    private RestTemplate restTemplate;

    //@Cacheable(value = "currentCart")
    @Override
    public CartDto getCart() {
        return currentCart;
    }

    @Override
    public Product addToCart(Product product) {
        product.setNumberInCart(product.getNumberInCart() + 1);
        List<Product> productList = getCart().getProducts();
        productList.add(product);
        getCart().setProducts(productList);
        log.info("product added , new cart size -> " + currentCart.getProducts().size());
        return product;
    }
}
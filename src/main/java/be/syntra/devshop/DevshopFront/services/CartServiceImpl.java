package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private CartDto currentCart;

    @Autowired
    public CartServiceImpl(CartDto currentCart) {
        this.currentCart = currentCart;
    }

    @Override
    public CartDto getCart() {
        if (currentCart == null) {
            currentCart = CartDto.builder()
                    .cartCreationDateTime(LocalDateTime.now())
                    .finalizedCart(false)
                    .activeCart(true)
                    .paidCart(false)
                    .build();
        }
        return currentCart;
    }

    @Override
    public Product addToCart(Product product) {
        List<Product> productList = getCart().getProducts();
        productList.add(product);
        getCart().setProducts(productList);
        return product;
    }
}

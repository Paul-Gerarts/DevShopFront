package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private CartDto currentCart;

    @Autowired
    public CartServiceImpl(CartDto currentCart) {
        this.currentCart = currentCart;
    }

    @Override
    public Product addToCart(Product product) {
        List<Product> productList = currentCart.getProducts();
        productList.add(product);
        currentCart.setProducts(productList);
        return product;
    }
}

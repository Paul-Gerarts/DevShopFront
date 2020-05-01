package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;

import java.math.BigDecimal;


public interface CartService {

    CartDto getCart();

    void addToCart(Product product);

    void addOneToProductInCart(Long productId);

    void removeOneFromProductInCart(Long productId);

    void removeProductFromCart(Long productId);

    StatusNotification payCart(String username);

    BigDecimal getCartTotalPrice();
}

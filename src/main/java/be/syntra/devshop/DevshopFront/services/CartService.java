package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;

public interface CartService {

    CartDto getCart();

    void addToCart(Product product);

    void addOneToProductInCart(Long productId);

    void removeOneFromProductInCart(Long productId);

    void removeProductFromCart(Long productId);
}

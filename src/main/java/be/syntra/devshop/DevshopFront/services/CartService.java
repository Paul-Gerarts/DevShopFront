package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;

public interface CartService {

    CartDto getCart();

    void addToCart(Product product);

    StatusNotification payCart(CartDto cartDto);
}

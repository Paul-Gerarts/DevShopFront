package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;

public interface CartService {

    CartDto getCart();

    void addToCart(Product product);

    StatusNotification payCart(CartDto cartDto);

    void cartTotalPrice(PaymentDto paymentDto);

    void addOneToProductInCart(Long productId);

    void removeOneFromProductInCart(Long productId);

    void removeProductFromCart(Long productId);
}

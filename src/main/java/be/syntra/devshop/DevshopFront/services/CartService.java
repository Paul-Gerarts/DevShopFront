package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;

import java.security.Principal;


public interface CartService {

    CartDto getCart();

    void addToCart(Product product);

    StatusNotification payCart(CartDto cartDto, Principal user);

    void cartTotalPrice(PaymentDto paymentDto);
}

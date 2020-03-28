package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;

public interface CartService {
    Product addToCart(Product product);
}

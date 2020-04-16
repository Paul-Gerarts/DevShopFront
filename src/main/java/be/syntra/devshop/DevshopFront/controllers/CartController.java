package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/cart")
public class CartController {
    final String CART_OVERVIEW_PAGE = "";
    CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/submit")
    public String submitCart() {
        log.info("submitCart");
        return CART_OVERVIEW_PAGE;
    }
}

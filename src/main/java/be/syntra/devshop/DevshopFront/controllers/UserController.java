package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private CartService cartService;

    @Autowired
    public UserController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String displayCartWhenLoggedIn() {
        log.info("displayCartWhenLoggedIn()");
        return "redirect:/users/cart/detail";
    }

    @GetMapping("/cart/overview")
    public String displayCartOverview(Model model) {
        PaymentDto paymentDto = new PaymentDto();
        cartTotalPrice(paymentDto);
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("payment", paymentDto);
        return "user/cartOverview";
    }

    private void cartTotalPrice(PaymentDto paymentDto) {
        paymentDto.setTotalPrice(cartService.getCart().getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @PostMapping("/cart/overview/paycart")
    public String payCart(Model model) {
        StatusNotification statusNotification = cartService.payCart(cartService.getCart());
        model.addAttribute("status", statusNotification);
        return "redirect:/products";
    }
}

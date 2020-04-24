package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

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
        cartService.cartTotalPrice(paymentDto);
        CartDto cartDto = cartService.getCart();
        model.addAttribute("cart", cartDto);
        model.addAttribute("payment", paymentDto);
        log.info("displayCartOverview() -> {}", cartDto.isActiveCart());
        return "user/cartOverview";
    }

    @PostMapping("/cart/overview")
    public String payCart(Model model, Principal user) {
        log.info(user.getName());
        PaymentDto paymentDto = new PaymentDto();
        cartService.cartTotalPrice(paymentDto);
        model.addAttribute("cart", cartService.getCart());
        StatusNotification statusNotification = cartService.payCart(cartService.getCart(), user);
        model.addAttribute("status", statusNotification);
        model.addAttribute("payment", paymentDto);
        return "user/cartOverview";
    }
}

package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private CartService cartService;

    @Autowired
    public UserController(CartService cartService) {
        this.cartService = cartService;
    }

    // todo: remove if no problems during testing
/*
    @GetMapping("/cart")
    public String displayCartWhenLoggedIn() {
        log.info("displayCartWhenLoggedIn()");
        return "redirect:/users/cart/detail";
    }*/

    @GetMapping("/cart/overview")
    public String displayCartOverview(Model model) {
        log.info("displayCartOverview()");
        PaymentDto paymentDto = new PaymentDto();
        cartService.cartTotalPrice(paymentDto);
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("payment", paymentDto);
        return "user/cartOverview";
    }

    @PostMapping("/cart/overview/paycart")
    public String payCart(Model model) {
        log.info("payCart()");
        StatusNotification statusNotification = cartService.payCart(cartService.getCart());
        model.addAttribute("status", statusNotification);
        return "redirect:/products";
    }

    @GetMapping("/cart/details/plus_one/{id}")
    public String addMoreToCart(@PathVariable Long id) {
        cartService.addOneToProductInCart(id);
        return "redirect:/users/cart/overview";
    }

    @GetMapping("/cart/details/minus_one/{id}")
    public String addLessToCart(@PathVariable Long id) {
        cartService.removeOneFromProductInCart(id);
        return "redirect:/users/cart/overview";
    }

    @GetMapping("/cart/details/delete/{id}")
    public String removeProductFromCart(@PathVariable Long id) {
        cartService.removeProductFromCart(id);
        return "redirect:/users/cart/overview";
    }
}

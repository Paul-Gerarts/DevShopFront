package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.PaymentOption;
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

import java.security.Principal;
import java.util.Arrays;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    private static final String PAYMENT = "payment";
    private static final String REDIRECT_CART_DETAIL_URL = "redirect:/users/cart/detail";

    private CartService cartService;

    @Autowired
    public UserController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String displayCartWhenLoggedIn() {
        log.info("displayCartWhenLoggedIn()");
        return REDIRECT_CART_DETAIL_URL;
    }

    @GetMapping("/cart/detail")
    public String displayCartOverview(Model model) {
        log.info("displayCartOverview()");
        model.addAttribute("cart", cartService.getCart());
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentOptions(Arrays.asList(PaymentOption.values()));
        cartService.cartTotalPrice(paymentDto);
        model.addAttribute(PAYMENT, paymentDto);
        return "user/cartOverview";
    }

    @GetMapping("/cart/details/plus_one/{id}")
    public String increaseCartProduct(@PathVariable Long id) {
        cartService.addOneToProductInCart(id);
        return REDIRECT_CART_DETAIL_URL;
    }

    @GetMapping("/cart/details/minus_one/{id}")
    public String decreaseCartProduct(@PathVariable Long id) {
        cartService.removeOneFromProductInCart(id);
        return REDIRECT_CART_DETAIL_URL;
    }

    @GetMapping("/cart/details/delete/{id}")
    public String removeCartProduct(@PathVariable Long id) {
        cartService.removeProductFromCart(id);
        return REDIRECT_CART_DETAIL_URL;
    }

    @PostMapping("/cart/detail")
    public String payCart(Model model, Principal user) {
        log.info(user.getName());
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentOptions(Arrays.asList(PaymentOption.values()));
        cartService.cartTotalPrice(paymentDto);
        StatusNotification statusNotification = cartService.payCart(user.getName());
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("status", statusNotification);
        model.addAttribute(PAYMENT, paymentDto);
        return "user/cartOverview";
    }
}

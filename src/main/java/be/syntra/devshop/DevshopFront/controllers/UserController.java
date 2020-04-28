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

    @GetMapping("/cart/detail")
    public String displayCartOverview(Model model) {
        log.info("displayCartOverview()");
        model.addAttribute("cart", cartService.getCart());
        PaymentDto paymentDto = new PaymentDto();
        cartService.cartTotalPrice(paymentDto);
        model.addAttribute("payment", paymentDto);
        return "user/cartOverview";
    }

    @GetMapping("/cart/details/plus_one/{id}")
    public String increaseCartProduct(@PathVariable Long id, Model model) {
        cartService.addOneToProductInCart(id);
        PaymentDto paymentDto = new PaymentDto();
        cartService.cartTotalPrice(paymentDto);
        model.addAttribute("payment", paymentDto);
        return "redirect:/users/cart/detail";
    }

    @GetMapping("/cart/details/minus_one/{id}")
    public String decreaseCartProduct(@PathVariable Long id) {
        cartService.removeOneFromProductInCart(id);
        PaymentDto paymentDto = new PaymentDto();
        cartService.cartTotalPrice(paymentDto);
        return "redirect:/users/cart/detail";
    }

    @GetMapping("/cart/details/delete/{id}")
    public String removeCartProduct(@PathVariable Long id) {
        cartService.removeProductFromCart(id);
        PaymentDto paymentDto = new PaymentDto();
        cartService.cartTotalPrice(paymentDto);
        return "redirect:/users/cart/detail";
    }

    @PostMapping("/cart/detail")
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

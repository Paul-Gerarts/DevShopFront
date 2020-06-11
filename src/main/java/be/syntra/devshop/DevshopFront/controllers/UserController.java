package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.PaymentOption;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartProductsDisplayDto;
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
    private static final String REDIRECT_CART_DETAIL_URL = "redirect:/users/cart/detail";

    private final CartService cartService;

    @Autowired
    public UserController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String displayCartWhenLoggedIn() {
        return REDIRECT_CART_DETAIL_URL;
    }

    @GetMapping("/cart/detail")
    public String displayCartOverview(Model model) {
        addModelAttributesOfCartAndPayment(model);
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
        StatusNotification statusNotification = cartService.payCart(user.getName());
        addModelAttributesOfCartAndPayment(model);
        model.addAttribute("status", statusNotification);
        return "user/cartOverview";
    }

    private void addModelAttributesOfCartAndPayment(Model model) {
        CartProductsDisplayDto cartProductsDisplayDto = cartService.getCartProductsDto();
        CartDto currentCart = cartService.getCart();
        PaymentDto paymentDto = PaymentDto.builder()
                .totalCartPrice(cartService.getCartTotalPrice(currentCart))
                .paymentOptions(Arrays.asList(PaymentOption.values()))
                .build();
        model.addAttribute("cart", cartProductsDisplayDto);
        model.addAttribute("payment", paymentDto);
    }
}

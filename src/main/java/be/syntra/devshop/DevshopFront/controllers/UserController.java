package be.syntra.devshop.DevshopFront.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    private final String CART_DETAIL_TEMPLATE = "redirect:/users/cart/detail";

    @GetMapping("/cart")
    public String displayCartWhenLoggedIn() {
        log.info("go to detail");
        return CART_DETAIL_TEMPLATE;
    }
}

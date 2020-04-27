package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.DataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/cart")
public class CartController {

    private final DataStore dataStore;

    @Autowired
    public CartController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/details")
    public String goToCartAfterLogin() {
        log.info("goToCartAfterLogin() -> redirect user to cart-details page after login/loginCheck");
        dataStore.getMap().put("redirectToCartAfterUserSuccessfulLogin", true);
        return "redirect:/users/cart/overview";
    }
}

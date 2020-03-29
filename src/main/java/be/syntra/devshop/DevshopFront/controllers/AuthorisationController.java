package be.syntra.devshop.DevshopFront.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/auth")
public class AuthorisationController {

    @GetMapping("/login")
    public RedirectView displayLoginPage(){
        return new RedirectView("/login");
    }

    @GetMapping("/register")
    public RedirectView displayRegisterForm(){
        return new RedirectView("/register");
    }
}

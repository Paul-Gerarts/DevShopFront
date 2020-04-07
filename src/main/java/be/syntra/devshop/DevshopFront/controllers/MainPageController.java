package be.syntra.devshop.DevshopFront.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping
public class MainPageController {

    @GetMapping
    public RedirectView displayMainPage(Model model) {
        return new RedirectView("/products");
    }
}

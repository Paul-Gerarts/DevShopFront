package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.dto.SearchDto;
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
        model.addAttribute("search", new SearchDto());
        return new RedirectView("/products");
    }
}

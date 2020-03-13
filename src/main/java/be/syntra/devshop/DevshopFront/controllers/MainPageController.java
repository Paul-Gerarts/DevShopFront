package be.syntra.devshop.DevshopFront.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/devshop")
public class MainPageController {

    @GetMapping
    public ModelAndView displayMainPage(){
        return new ModelAndView("index");
    }
}

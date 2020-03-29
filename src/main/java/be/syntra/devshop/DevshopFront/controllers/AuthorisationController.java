package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import be.syntra.devshop.DevshopFront.services.AuthorisationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping()
public class AuthorisationController {

    private AuthorisationServiceImpl authorisationService;

    @Autowired
    public AuthorisationController (AuthorisationServiceImpl authorisationService){
        this.authorisationService = authorisationService;
    }

    @GetMapping("/auth/login")
    public RedirectView displayLoginPage(){
        return new RedirectView("/login");
    }

    @GetMapping("/register")
    public String displayRegisterForm(Model model){
        model.addAttribute("user", new RegisterUserDto());
        return "/user/register";
    }

    @PostMapping("/register")
    public Object getRegisterFormEntry(@ModelAttribute("registerForm")RegisterUserDto registerUserDto, Model model){
        StatusNotification statusNotification = authorisationService.register(registerUserDto);
        model.addAttribute("user", registerUserDto);
        model.addAttribute("status", statusNotification);
        return !statusNotification.equals(StatusNotification.SUCCES)
                ? "/user/register"
                : new RedirectView("products");
    }
}

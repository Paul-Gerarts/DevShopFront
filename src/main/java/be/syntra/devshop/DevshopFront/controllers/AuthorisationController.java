package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import be.syntra.devshop.DevshopFront.services.AuthorisationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

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
    public String getRegisterFormEntry(@Valid @ModelAttribute("registerForm") RegisterUserDto registerUserDto, Model model, BindingResult bindingResult) {
        StatusNotification statusNotification = authorisationService.registerIfHasNoErrors(registerUserDto, bindingResult);
        model.addAttribute("user", registerUserDto);
        model.addAttribute("status", statusNotification);
        return (!statusNotification.equals(StatusNotification.SUCCES))
                ? "/user/register"
                : "redirect:/products";
    }
}

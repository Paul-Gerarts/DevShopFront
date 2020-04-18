package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import be.syntra.devshop.DevshopFront.services.AuthorisationService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
@RequestMapping()
public class AuthorisationController {

    private static final String REGISTER_FORM = "/user/register";

    private final AuthorisationService authorisationService;

    @Autowired
    public AuthorisationController(AuthorisationService authorisationService) {
        this.authorisationService = authorisationService;
    }

    @GetMapping("/auth/login")
    public RedirectView displayLoginPage() {
        return new RedirectView("/login");
    }

    @GetMapping("/register")
    public String displayRegisterForm(Model model) {
        model.addAttribute("user", new RegisterUserDto());
        return REGISTER_FORM;
    }

    @PostMapping("/register")
    public String getRegisterFormEntry(@Valid @ModelAttribute("user") RegisterUserDto registerUserDto, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            StatusNotification statusNotification = authorisationService.register(registerUserDto);
            model.addAttribute("user", registerUserDto);
            model.addAttribute("status", statusNotification);
            return (!statusNotification.equals(StatusNotification.SUCCESS))
                    ? REGISTER_FORM
                    : "redirect:/products";
        }
        return REGISTER_FORM;
    }
}

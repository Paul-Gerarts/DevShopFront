package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.dtos.ErrorDto;
import be.syntra.devshop.DevshopFront.services.ErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class MyErrorController implements ErrorController {

    private final ErrorService errorService;

    @Autowired
    public MyErrorController(ErrorService errorService) {
        this.errorService = errorService;
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        return displayCustomErrorPage(request, model);
    }

    /*
     * Getting rid of that annoying GET-error for favicon.ico
     */
    @GetMapping("/favicon.ico")
    public String handleServletError(HttpServletRequest request, Model model) {
        return displayCustomErrorPage(request, model);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private String displayCustomErrorPage(HttpServletRequest request, Model model) {
        ErrorDto thrownError = errorService.determineError(request);
        model.addAttribute("thrownError", thrownError);
        return "fragments/whitelabelError";
    }
}

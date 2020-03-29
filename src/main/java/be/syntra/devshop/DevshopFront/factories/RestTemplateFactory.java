package be.syntra.devshop.DevshopFront.factories;

import be.syntra.devshop.DevshopFront.exceptions.RestTemplateResponseErrorHandler;
import be.syntra.devshop.DevshopFront.security.services.SecurityLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory {

    @Autowired
    private SecurityLoginService securityLoginService;

    public RestTemplate ofSecurity(){
        return new RestTemplateBuilder()
        .errorHandler(new RestTemplateResponseErrorHandler())
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + securityLoginService.login())
        .build();
    }
}

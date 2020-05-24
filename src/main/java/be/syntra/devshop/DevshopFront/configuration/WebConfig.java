package be.syntra.devshop.DevshopFront.configuration;

import be.syntra.devshop.DevshopFront.exceptions.RestTemplateResponseErrorHandler;
import be.syntra.devshop.DevshopFront.models.DataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RestTemplateHeaderModifierInterceptor interceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/css/**",
                "/js/**")
                .addResourceLocations(
                        "classpath:/static/css/",
                        "classpath:/static/js/");
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .additionalInterceptors(Collections.singleton(interceptor))
                .build();
    }

    @Bean
    @SessionScope
    public DataStore getDataStore() {
        Map<String, Boolean> dataStore = new HashMap<>();
        dataStore.put("redirectToCartAfterUserSuccessfulLogin", false);
        return new DataStore(dataStore);
    }
}


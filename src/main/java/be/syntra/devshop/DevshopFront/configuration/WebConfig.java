package be.syntra.devshop.DevshopFront.configuration;

import be.syntra.devshop.DevshopFront.exceptions.RestTemplateResponseErrorHandler;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.UpdateProductCache;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.models.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
                .interceptors(Collections.singleton(interceptor))
                .build();
    }

    @Bean
    public CartDto getCurrentCart() {
        List<Product> productList = new ArrayList<>();
        return CartDto.builder()
                .cartCreationDateTime(LocalDateTime.now())
                .finalizedCart(false)
                .activeCart(true)
                .paidCart(false)
                .products(productList)
                .build();
    }

    // todo: DEV-015 might not create the SearchModelDto here

    @Bean
    public SearchDto getSearchModelDto() {
        return new SearchDto();
    }

    @Bean
    public ProductListCache getProductListCache() {
        return new ProductListCache();
    }

    @Bean
    public UpdateProductCache getUpdateProductCache() {
        return UpdateProductCache.builder().build();
    }

    @Bean
    public HashMap<String, Boolean> dataStore() {

    }
}


package be.syntra.devshop.DevshopFront.TestUtils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.models.dto.SearchDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class TestWebConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CartDto currentCart() {
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
}

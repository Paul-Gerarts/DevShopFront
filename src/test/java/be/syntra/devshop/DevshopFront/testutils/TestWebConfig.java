package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .paidCart(false)
                //.products(productList)
                .productDtos(new ArrayList<ProductDto>())
                .build();
    }

    @Bean
    public SearchModel getSearchModel() {
        return new SearchModel();
    }

    @Bean
    public DataStore getDataStore() {
        Map<String, Boolean> dataStore = new HashMap<>();
        dataStore.put("redirectToCartAfterUserSuccessfulLogin", false);
        return new DataStore(dataStore);
    }
}
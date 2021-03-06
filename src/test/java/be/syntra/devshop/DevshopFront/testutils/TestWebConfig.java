package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@TestConfiguration
public class TestWebConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CartDto currentCart() {
        return CartDto.builder()
                .cartCreationDateTime(LocalDateTime.now())
                .finalizedCart(false)
                .paidCart(false)
                .cartProductDtoSet(new HashSet<>())
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
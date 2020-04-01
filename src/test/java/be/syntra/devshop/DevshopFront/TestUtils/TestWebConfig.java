package be.syntra.devshop.DevshopFront.TestUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class TestWebConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

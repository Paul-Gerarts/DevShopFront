package be.syntra.devshop.DevshopFront.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/css/**",
                "/js/**")
                .addResourceLocations(
                        "classpath:/static/css/",
                        "classpath:/static/js/"
                );
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

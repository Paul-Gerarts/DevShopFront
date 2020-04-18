package be.syntra.devshop.DevshopFront.security.services;

import be.syntra.devshop.DevshopFront.models.dtos.JWTTokenDto;
import be.syntra.devshop.DevshopFront.models.dtos.LogInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SecurityLoginService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${backend.userName}")
    private String userName;

    @Value("${backend.password}")
    private String password;

    private final RestTemplate restTemplate;

    @Autowired
    public SecurityLoginService (
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "token")
    public String login(){
        LogInDto request = LogInDto.builder()
                .userName(userName)
                .password(password)
                .build();
        JWTTokenDto jwtTokenDto = restTemplate.postForEntity(baseUrl + "/auth/login", request, JWTTokenDto.class).getBody();
        return jwtTokenDto.getToken();
    }
}

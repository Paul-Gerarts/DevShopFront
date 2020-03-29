package be.syntra.devshop.DevshopFront.security.services;

import be.syntra.devshop.DevshopFront.models.dto.JWTTokenDto;
import be.syntra.devshop.DevshopFront.models.dto.LogInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
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

    private RestTemplate restTemplate;

    @Autowired
    public SecurityLoginService (
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    @CacheEvict(value = "token", allEntries = true)
    public String login(){
        LogInDto request = LogInDto.builder()
                .userName(userName)
                .password(password)
                .build();
        JWTTokenDto jwtTokenDto = restTemplate.postForEntity(baseUrl + "/auth/login", request, JWTTokenDto.class).getBody();
        return jwtTokenDto.getToken();
    }
}

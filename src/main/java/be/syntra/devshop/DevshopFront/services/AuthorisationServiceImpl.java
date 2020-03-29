package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.factories.RestTemplateFactory;
import be.syntra.devshop.DevshopFront.factories.UserFactory;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;

@Service
public class AuthorisationServiceImpl implements AuthorisationService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${registerEndpoint}")
    private String endpoint;

    private Logger logger = LoggerFactory.getLogger(AuthorisationServiceImpl.class);

    private String resourceUrl = null;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateFactory restTemplateFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private UserRoleService userRoleService;

    @PostConstruct
    private void init() {
        resourceUrl = baseUrl.concat(endpoint);
        restTemplate = restTemplateFactory.ofSecurity();
    }

    @Override
    public StatusNotification register(RegisterUserDto registerUserDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterUserDto> request = new HttpEntity<>(registerUserDto, httpHeaders);

        if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmedPassword())) {
            return StatusNotification.PASSWORD_NO_MATCH;
        }

        try {
            ResponseEntity<RegisterUserDto> loginDtoResponseEntity = restTemplate.postForEntity(resourceUrl, request, RegisterUserDto.class);
            if (HttpStatus.CREATED.equals(loginDtoResponseEntity.getStatusCode())) {
                logger.info("register() -> succesfull {}", registerUserDto.getUserName());
                createNewUserLogin(registerUserDto);
                return StatusNotification.SUCCES;
            }
        } catch (Exception e) {
            logger.error("register() -> {}", e.getLocalizedMessage());
        }
        return StatusNotification.REGISTER_FAIL;
    }

    private void createNewUserLogin(RegisterUserDto registerUserDto) {
        userRepository.save(
                userFactory.ofSecurity(
                        List.of(userRoleService.findByRoleName(ROLE_USER.name())),
                        registerUserDto.getUserName(),
                        new BCryptPasswordEncoder().encode(registerUserDto.getPassword()),
                        registerUserDto.getFirstName() + " " + registerUserDto.getLastName())
        );
    }

}

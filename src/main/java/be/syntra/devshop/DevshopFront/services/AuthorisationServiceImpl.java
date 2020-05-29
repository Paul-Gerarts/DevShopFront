package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.factories.UserFactory;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.RegisterUserDto;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;

@Service
@Slf4j
public class AuthorisationServiceImpl implements AuthorisationService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${registerEndpoint}")
    private String endpoint;

    private String resourceUrl = null;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private UserRoleService userRoleService;

    @PostConstruct
    private void init() {
        resourceUrl = baseUrl.concat(endpoint);
    }

    @Override
    public StatusNotification register(@Valid RegisterUserDto registerUserDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterUserDto> request = new HttpEntity<>(registerUserDto, httpHeaders);

        if (verifiedPasswordAndPasswordDoNotMatch(registerUserDto)) {
            return StatusNotification.PASSWORD_NO_MATCH;
        }

            ResponseEntity<RegisterUserDto> loginDtoResponseEntity = restTemplate.postForEntity(resourceUrl, request, RegisterUserDto.class);
            if (HttpStatus.CREATED.equals(loginDtoResponseEntity.getStatusCode())) {
                createNewUserLogin(registerUserDto);
                return StatusNotification.SUCCESS;
            }
        return StatusNotification.REGISTER_FAIL;
    }

    private boolean verifiedPasswordAndPasswordDoNotMatch(@Valid RegisterUserDto registerUserDto) {
        return !registerUserDto.getPassword().equals(registerUserDto.getConfirmedPassword());
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

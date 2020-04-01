package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.factories.UserFactory;
import be.syntra.devshop.DevshopFront.models.SecurityUser;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import be.syntra.devshop.DevshopFront.services.AuthorisationServiceImpl;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.UserRoleService;
import be.syntra.devshop.DevshopFront.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;
import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorisationController.class)
@Import(JWTTokenExceptionHandler.class)
public class AuthorisationControllerTest {

    private UserFactory userFactory = new UserFactory();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTTokenExceptionHandler jwtTokenExceptionHandler;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorisationServiceImpl authService;

    @MockBean
    private UserRoleService userRoleService;

    @Test
    void userCanLoginTest() throws Exception {
        //given
        String userName = "test@email.com";
        String password = "password";
        String fullName = "full name";
        SecurityUser dummyUser = userFactory.ofSecurity(
                Collections.singletonList(userRoleService.findByRoleName(ROLE_USER.name())),
                userName,
                password,
                fullName);
        when(userRepository.findByUserName(userName)).thenReturn(ofNullable(dummyUser));

        //when
        final ResultActions getResult = mockMvc.perform(get("/login"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(userRoleService, times(1)).findByRoleName(ROLE_USER.name());
    }

    @Test
    void userCanRegisterTest() throws Exception {
        // given
        RegisterUserDto dummyRegisterDto = RegisterUserDto.builder()
                .firstName("Test")
                .lastName("McTest")
                .userName("test@email.com")
                .password("test")
                .confirmedPassword("test")
                .street("street")
                .number("1")
                .boxNumber("")
                .postalCode("3500")
                .city("city")
                .country("Belgium")
                .build();
        when(authService.register(dummyRegisterDto)).thenReturn(StatusNotification.SUCCES);

        // when
        final ResultActions getPostResult = mockMvc.perform(post("/register")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("firstName", dummyRegisterDto.getFirstName())
                .param("lastName", dummyRegisterDto.getLastName())
                .param("userName", dummyRegisterDto.getUserName())
                .param("password", dummyRegisterDto.getPassword())
                .param("confirmedPassword", dummyRegisterDto.getConfirmedPassword())
                .param("street", dummyRegisterDto.getStreet())
                .param("number", dummyRegisterDto.getNumber())
                .param("boxNumber", dummyRegisterDto.getBoxNumber())
                .param("postalCode", dummyRegisterDto.getPostalCode())
                .param("city", dummyRegisterDto.getCity())
                .param("country", dummyRegisterDto.getCountry()));

        // TODO add getGetResult for "/users" to verify that indeed users are registered. To be implemented by DEV-011

        // then
        getPostResult.andExpect(status().isFound());
        verify(authService, times(1)).register(dummyRegisterDto);
    }
}

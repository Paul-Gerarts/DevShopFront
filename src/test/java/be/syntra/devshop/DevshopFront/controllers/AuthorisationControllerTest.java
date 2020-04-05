package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.TestUtils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.TestUtils.TestWebConfig;
import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.factories.UserFactory;
import be.syntra.devshop.DevshopFront.models.SecurityUser;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import be.syntra.devshop.DevshopFront.services.AuthorisationServiceImpl;
import be.syntra.devshop.DevshopFront.services.UserRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;
import static java.util.Optional.ofNullable;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthorisationController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
public class AuthorisationControllerTest {

    private UserFactory userFactory = new UserFactory();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

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
        when(authService.register(dummyRegisterDto)).thenReturn(StatusNotification.SUCCESS);

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

        // then
        getPostResult
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/products"));
        verify(authService, times(1)).register(dummyRegisterDto);
    }

    @Test
    void canGetRegisterFormTest() throws Exception {
        //given

        //when
        final ResultActions getResult = mockMvc.perform(get("/register"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("/user/register"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Register form")))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", new RegisterUserDto()));
    }

    @Test
    void canRedirectToLoginPageTest() throws Exception {
        // given

        // when
        final ResultActions getResult = mockMvc.perform(get("/auth/login"));

        // then
        getResult
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }
}

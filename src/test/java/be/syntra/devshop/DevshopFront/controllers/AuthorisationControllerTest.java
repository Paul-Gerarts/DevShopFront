package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.factories.UserFactory;
import be.syntra.devshop.DevshopFront.models.SecurityUser;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.UserRoleService;
import be.syntra.devshop.DevshopFront.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;
import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
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
    private UserRoleService userRoleService;

    @Test
    void userCanLoginTest() throws Exception {
        // FIXME https://stackoverflow.com/questions/41142871/test-http-status-code-of-redirected-url-with-mockmvc
        // FIXME https://www.mock-server.com/

        //given
        String userName = "test@email.com";
        String password = "password";
        String fullName = "full name";
        SecurityUser dummyUser = userFactory.ofSecurity(
                Collections.singletonList(userRoleService.findByRoleName(ROLE_USER.name())),
                userName,
                password,
                fullName);
        Mockito.when(userRepository.findByUserName(userName)).thenReturn(ofNullable(dummyUser));

        //when
        final ResultActions getResult = mockMvc.perform(get("/auth/login"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().contentType("text/html;charset=UTF-8"));

        verify(userRepository, times(1)).findByUserName(userName);
    }
}

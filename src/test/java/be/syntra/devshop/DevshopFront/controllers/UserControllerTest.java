package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.PaymentDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.testutils.CartUtils;
import be.syntra.devshop.DevshopFront.testutils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
class UserControllerTest {
    @MockBean
    private CartService cartService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void displayCartWhenLoggedIn() throws Exception {
        // when
        final ResultActions getResult = mockMvc.perform(get("/users/cart"));

        // then
        getResult.andExpect(status().isFound());
    }

    @Test
    void testDisplayCartOverview() throws Exception {
        //given
        PaymentDto paymentDto = new PaymentDto();
        final CartDto dummyCartDto = CartUtils.getCartWithOneDummyProduct();
        when(cartService.getCart()).thenReturn(dummyCartDto);

        //when
        final ResultActions getResult = mockMvc.perform(get("/users/cart/overview"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("user/cartOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("payment", paymentDto))
                .andExpect(model().attribute("cart", dummyCartDto));

    }
}
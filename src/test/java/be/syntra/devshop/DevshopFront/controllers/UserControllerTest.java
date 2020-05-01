package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.testutils.CartUtils;
import be.syntra.devshop.DevshopFront.testutils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private Principal principal;

    @MockBean
    CartService cartService;

    @Test
    @WithMockUser(roles = {"USER"})
    void displayCartWhenLoggedIn() throws Exception {
        // given
        when(cartService.getCart()).thenReturn(CartUtils.getCartWithOneDummyProduct());

        // when
        final ResultActions getResult = mockMvc.perform(get("/users/cart/overview"));

        // then
        getResult.andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("user/cartOverview"));

        verify(cartService, times(1)).getCart();
    }

    @Test
    void displayLoginWhenRequestingCartAndNotLoggedIn() throws Exception {
        // when
        final ResultActions getResult = mockMvc.perform(get("/users/cart/overview"));

        // then
        getResult
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void canDoAddOneToProductTotalInCart() throws Exception {
        // when
        final ResultActions getResult = mockMvc.perform(get("/users/cart/details/plus_one/1"));

        // then
        getResult
                .andExpect(redirectedUrl("/users/cart/overview"));

        verify(cartService).addOneToProductInCart(1L);
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void canDoRemoveOneFromProductTotalInCart() throws Exception {
        // when
        final ResultActions getResult = mockMvc.perform(get("/users/cart/details/minus_one/2"));

        // then
        getResult
                .andExpect(redirectedUrl("/users/cart/overview"));

        verify(cartService).removeOneFromProductInCart(2L);
    }

    @Test
    void testDisplayCartOverview() throws Exception {
        //given

        final CartDto dummyCartDto = CartUtils.getCartWithOneDummyProduct();
        when(cartService.getCart()).thenReturn(dummyCartDto);

        //when
        final ResultActions getResult = mockMvc.perform(get("/users/cart/overview"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("user/cartOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attribute("cart", dummyCartDto));

    }

    @Test
    @WithMockUser(roles = {"USER"})
    void canRemoveProductFromCart() throws Exception {
        // when
        final ResultActions getResult = mockMvc.perform(get("/users/cart/details/delete/3"));

        // then
        getResult
                .andExpect(redirectedUrl("/users/cart/overview"));

        verify(cartService).removeProductFromCart(3L);
    }

    @Test
    @WithMockUser(roles = {"User"})
    void canPayCart() throws Exception {
        //given
        final CartDto cartDto = CartUtils.getCartWithOneDummyProduct();
        StatusNotification statusNotification;
        when(statusNotification = cartService.payCart(cartDto, principal)).thenReturn(statusNotification);
        when(cartService.getCart()).thenReturn(cartDto);

        //when
        final ResultActions postResult = mockMvc.perform(
                post("/cart")
                        .contentType(MediaType.MULTIPART_FORM_DATA));

        // then
        postResult
                .andExpect(status().isOk())
                .andExpect(view().name("user/cartOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("cart", cartDto))
                .andExpect(model().attribute("status", statusNotification));

        verify(cartService, times(1)).payCart(cartDto, principal);
    }
}

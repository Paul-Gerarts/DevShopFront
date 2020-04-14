package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.TestUtils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.TestUtils.TestWebConfig;
import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.dtos.ErrorDto;
import be.syntra.devshop.DevshopFront.services.ErrorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MyErrorController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
public class MyErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorService errorService;

    @Test
    @WithMockUser
    void displayCustomErrorPageTest() throws Exception {
        //given
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode("404 NOT FOUND")
                .message("We couldn't find what you're searching for")
                .build();
        when(errorService.determineError(any())).thenReturn(errorDto);

        //when
        final ResultActions getResult = mockMvc.perform(get("/error"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("fragments/whitelabelError"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("We're working on it, perhaps you'd like to try something else?")))
                .andExpect(model().attributeExists("thrownError"))
                .andExpect(model().attribute("thrownError", errorDto));

        verify(errorService, times(1)).determineError(any());
    }

    @Test
    @WithMockUser
    void redirectToCustomErrorPageTest() throws Exception {
        // given
        ErrorDto errorDto = ErrorDto.builder()
                .errorCode("404 NOT FOUND")
                .message("We couldn't find what you're searching for")
                .build();
        when(errorService.determineError(any())).thenReturn(errorDto);

        // when
        final ResultActions getResult = mockMvc.perform(get("/favicon.ico"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("fragments/whitelabelError"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("We're working on it, perhaps you'd like to try something else?")))
                .andExpect(model().attributeExists("thrownError"))
                .andExpect(model().attribute("thrownError", errorDto));

        verify(errorService, times(1)).determineError(any());
    }
}

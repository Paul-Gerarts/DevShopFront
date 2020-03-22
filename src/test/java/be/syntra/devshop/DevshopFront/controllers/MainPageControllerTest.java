package be.syntra.devshop.DevshopFront.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainPageController.class)
class MainPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void displayMainPageWithTitle() throws Exception {
        // given

        // when
        final ResultActions getResult = mockMvc.perform(get("/devshop"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("DevShop")));
    }
}
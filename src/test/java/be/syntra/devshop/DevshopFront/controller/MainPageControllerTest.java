package be.syntra.devshop.DevshopFront.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MainPageController.class)
@AutoConfigureMockMvc
class MainPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void displayMainPageWithTitle() throws Exception {
        mockMvc.perform(get("/devshop"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("DevShop")));
    }
}
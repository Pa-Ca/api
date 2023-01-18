package com.paca.paca.controller;

import com.paca.paca.config.SecurityConfig;
import com.paca.paca.service.TokenService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest({HomeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
class HomeControllerTest {

     @Autowired
    MockMvc mvc;

    @Test
    @Disabled
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isUnauthorized());
    }

    @Test
    @Disabled
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        MvcResult result = this.mvc.perform(post("/token")
            .with(httpBasic("amin", "password")))
            .andExpect(status().isOk())
            .andReturn();

        String token = result.getResponse().getContentAsString();
        this.mvc.perform(
            get("/api/hello").header("Authorization", "Bearer " + token))
            .andExpect(content().string("Hello World")
        );
    }

    @Test
    @Disabled
    @WithMockUser
    public void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/api/hello")).andExpect(status().isOk());
    }

}
package com.egov.demo_egov.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sayHello_shouldReturnHelloWorld() throws Exception {
        // When & Then
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World!"));
    }

    @Test
    void sayHello_shouldReturnCorrectContentType() throws Exception {
        // When & Then
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void sayHello_shouldReturnExpectedMessage() throws Exception {
        // Given
        String expectedMessage = "Hello World!";

        // When & Then
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }
}
package com.ibank.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpMediaTypeNotSupportedException;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String registerURL = "http://192.168.0.21:8888/api/v1/users/registration";

    @Test
    public void registerShouldFailWhenNotJsonIsPost() throws Exception {
        // given
        String plainText = "Hello";

        // when
        // then
        mockMvc.perform(post(registerURL)
                .content(plainText)
                .contentType(MediaType.TEXT_PLAIN))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMediaTypeNotSupportedException));

        //assertThrows(
        //    HttpMediaTypeNotSupportedException.class,
        //    () -> mockMvc.perform(post(registerURL)
        //        .content(plainText)
        //        .contentType(MediaType.TEXT_PLAIN)
        //    ).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn()
        //);

        //.andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMediaTypeNotSupportedException));

    }

    @Test
    public void registerShouldWorkWhenJsonIsPost() throws Exception {
        // given
        String json = """
            { 
                "id": null, 
                "username": "demo1", 
                "email": "demo1@gmail.com", 
                "password" : "1234"  
            }
            """;

        // when
        // then
        mockMvc.perform(post(registerURL)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void registerShouldFailWhenNotUsingPost() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get(registerURL))
            .andExpect(status().is(405));

        mockMvc.perform(put(registerURL))
            .andExpect(status().is(405));

        mockMvc.perform(head(registerURL))
            .andExpect(status().is(405));

        mockMvc.perform(delete(registerURL))
            .andExpect(status().is(405));
    }
}
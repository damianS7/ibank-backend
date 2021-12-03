package com.ibank.user;

import com.ibank.user.http.UserSignupRequest;
import com.ibank.user.http.UserSignupResponse;
import com.ibank.utils.ObjectJson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotSupportedException;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController underTest;

    private final String signupUrl = "http://192.168.0.21:8888/api/v1/users/signup";

    @Test
    public void signupShouldWork() throws Exception {
        // given
        UserSignupRequest signupRequest = new UserSignupRequest(
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        ResultActions rt = mockMvc.perform(post(signupUrl)
            .content(ObjectJson.toJson(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        String jsonResponse = rt.andReturn().getResponse().getContentAsString();
        UserSignupResponse signupResponse = ObjectJson.fromJson(jsonResponse, UserSignupResponse.class);

        // then
        assertThat(rt.andExpect(status().is(200)));
        assertThat(signupResponse.id).isGreaterThanOrEqualTo(0);
        assertThat(signupResponse.username).isEqualTo(signupRequest.username);
    }

    @Test
    public void signupShouldFailWhenFormNotValid() throws Exception {
        // given
        UserSignupRequest signupRequest = new UserSignupRequest(
            "",
            "demo@gmail.com",
            "1234"
        );

        // when
        ResultActions rt = mockMvc.perform(post(signupUrl)
            .content(ObjectJson.toJson(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        assertThat(rt.andExpect(status().is4xxClientError()));
    }

    @Test
    public void registerShouldFailWhenNotJsonIsPost() throws Exception {
        // given
        String plainText = "Hello";

        // when
        // then
        mockMvc.perform(post(signupUrl)
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
                "username": "demo", 
                "email": "demo@gmail.com", 
                "password" : "1234"  
            }
            """;

        // when
        // then
        mockMvc.perform(post(signupUrl)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void registerShouldFailWhenNotUsingPost() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get(signupUrl))
            .andExpect(status().is(405));

        mockMvc.perform(put(signupUrl))
            .andExpect(status().is(405));

        mockMvc.perform(head(signupUrl))
            .andExpect(status().is(405));

        mockMvc.perform(delete(signupUrl))
            .andExpect(status().is(405));
    }
}
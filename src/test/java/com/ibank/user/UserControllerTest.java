package com.ibank.user;

import com.ibank.auth.AuthenticationResponse;
import com.ibank.user.http.UserSignupRequest;
import com.ibank.user.http.UserSignupResponse;
import com.ibank.user.http.UserUpdateRequest;
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
    private final String updateUrl = "http://192.168.0.21:8888/api/v1/users/";

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
    public void signupShouldFailWhenNotJsonIsPost() throws Exception {
        // given
        String plainText = "Hello";

        // when
        ResultActions rt = mockMvc.perform(post(signupUrl)
            .content(plainText)
            .contentType(MediaType.TEXT_PLAIN)
        );

        // then
        assertThat(rt.andExpect(
            result -> assertTrue(result.getResolvedException() instanceof HttpMediaTypeNotSupportedException))
        );
    }

    @Test
    public void signupShouldWorkWhenJsonIsPost() throws Exception {
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
    public void signupShouldFailWhenNotUsingPost() throws Exception {
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

    @Test
    @Disabled
    public void updateShouldWork() throws Exception {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        ResultActions signup = mockMvc.perform(post(signupUrl)
            .content(ObjectJson.toJson(new UserSignupRequest(user)))
            .contentType(MediaType.APPLICATION_JSON)
        );

        String jsonResponse = signup.andReturn().getResponse().getContentAsString();
        AuthenticationResponse authResponse = ObjectJson.fromJson(jsonResponse, AuthenticationResponse.class);

        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "demo",
            "demo@gmail.com",
            "1234",
            "123456",
            authResponse.token
        );

        ResultActions update = mockMvc.perform(put(updateUrl)
            .content(ObjectJson.toJson(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print()
        );

        // then
        update.andExpect(status().is2xxSuccessful());
    }

    @Test
    @Disabled
    public void updateShouldFailWhenFormNotValid() throws Exception {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        ResultActions signup = mockMvc.perform(post(signupUrl)
            .content(ObjectJson.toJson(new UserSignupRequest(user)))
            .contentType(MediaType.APPLICATION_JSON)
        );

        String jsonResponse = signup.andReturn().getResponse().getContentAsString();
        AuthenticationResponse authResponse = ObjectJson.fromJson(jsonResponse, AuthenticationResponse.class);

        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "demo",
            "demo@gmail.com",
            "",
            "123456",
            authResponse.token
        );

        ResultActions updateResult = mockMvc.perform(put(updateUrl)
            .content(ObjectJson.toJson(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        assertThat(updateResult.andExpect(status().is4xxClientError()));
    }

    @Test
    @Disabled
    void updateShouldFailWhenFormUserIsDifferentThanLogged() throws Exception {
        // given
        User user = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "demo",
            "demo7777@gmail.com",
            "1234",
            "123456",
            "1234"
        );

        // when
        underTest.createUser(new UserSignupRequest(user));
        ResultActions updateResult = mockMvc.perform(put(updateUrl)
            .content(ObjectJson.toJson(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        // Debe arrojar IllegalState ya que el password actual es 123456 y recibe dummypassword
        //assertThrows(IllegalStateException.class, () -> {
        //    User updatedUser = underTest.updateUser(updateRequest);
        //    assertEquals(updatedUser.getEmail(), updateRequest.email);
        //});


        // then
        assertThat(updateResult.andExpect(status().is4xxClientError()));

        //log.info(" ...");
    }
}
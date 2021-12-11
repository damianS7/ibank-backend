package com.ibank.user;

import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
import com.ibank.user.http.UserSignupRequest;
import com.ibank.user.http.UserSignupResponse;
import com.ibank.user.http.UserUpdateRequest;
import com.ibank.user.http.UserUpdateResponse;
import com.ibank.utils.ObjectJson;
import com.ibank.utils.PasswordEncoder;
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
    private UserRepository userRepository;

    @Autowired
    private UserController underTest;

    private final String signupUrl = "http://192.168.0.21:8888/api/v1/signup";
    private final String updateUrl = "http://192.168.0.21:8888/api/v1/user/";
    private final String loginUrl = "http://192.168.0.21:8888/api/v1/login";

    @BeforeEach
    void setUp() {
        // Usuario por defecto
        userRepository.save(
            new User(
                null,
                "demox",
                "demox@gmail.com",
                PasswordEncoder.encode("1234")
            )
        );
    }

    @Test
    public void signupShouldWork() throws Exception {
        // given
        UserSignupRequest signupRequest = new UserSignupRequest(
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        ResultActions signupRequestResult = mockMvc.perform(post(signupUrl)
            .content(ObjectJson.toJson(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        signupRequestResult.andExpect(status().is(200));

        assertDoesNotThrow(() -> {
            UserSignupResponse signupResponse = ObjectJson.fromJson(
                signupRequestResult.andReturn().getResponse().getContentAsString(), UserSignupResponse.class
            );
            assertThat(signupResponse.id).isGreaterThanOrEqualTo(0);
            assertThat(signupResponse.username).isEqualTo(signupRequest.username);
        });
    }

    @Test
    // Mostrar algun error con InvalidForm ...
    public void signupShouldFailWhenFormNotValid() throws Exception {
        // given
        UserSignupRequest signupRequest = new UserSignupRequest(
            "",
            "demo@gmail.com",
            "1234"
        );

        // when
        ResultActions signupRequestResult = mockMvc.perform(post(signupUrl)
            .content(ObjectJson.toJson(signupRequest))
            .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        // then
        signupRequestResult.andExpect(status().is4xxClientError());
    }

    @Test
    public void signupShouldFailWhenNotJsonIsPost() throws Exception {
        // given
        String plainText = "Hello";

        // when
        ResultActions signupRequestResult = mockMvc.perform(post(signupUrl)
            .content(plainText)
            .contentType(MediaType.TEXT_PLAIN)
        );

        // then
        signupRequestResult.andExpect(
            result -> assertTrue(result.getResolvedException() instanceof HttpMediaTypeNotSupportedException)
        );
    }


    @Test
    public void signupShouldFailWhenNotUsingPostMethod() throws Exception {
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
    public void userUpdateShouldWork() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
            "demox", "1234"
        );

        ResultActions authenticationRequestResult = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // La respuesta en json
        String jsonResponse = authenticationRequestResult.andReturn().getResponse().getContentAsString();

        // Conversion del json a AuthenticationResponse
        AuthenticationResponse authenticationResponse = ObjectJson.fromJson(jsonResponse, AuthenticationResponse.class);

        // Request para modificar el usuario
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "demox",
            "demoMod@gmail.com",
            "1234",
            "123456",
            authenticationResponse.token
        );

        // when
        ResultActions updateRequestResult = mockMvc.perform(put(updateUrl)
            .content(ObjectJson.toJson(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + authenticationResponse.token)
        ).andDo(print());

        // then
        updateRequestResult.andExpect(status().is2xxSuccessful());

        assertDoesNotThrow(() -> {
            String updateRequestJsonResponse = updateRequestResult.andReturn().getResponse().getContentAsString();
            UserUpdateResponse userUpdateResponse = ObjectJson.fromJson(updateRequestJsonResponse, UserUpdateResponse.class);
            assertEquals(userUpdateResponse.email, "demoMod@gmail.com");
        });
    }

    @Test
    public void userUpdateShouldFailWhenFormNotValid() throws Exception {
        // given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
            "demox", "1234"
        );

        ResultActions authenticationRequestResult = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authenticationRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // La respuesta en json
        String jsonResponse = authenticationRequestResult.andReturn().getResponse().getContentAsString();

        // Conversion del json a AuthenticationResponse
        AuthenticationResponse authenticationResponse = ObjectJson.fromJson(jsonResponse, AuthenticationResponse.class);

        // Request para modificar el usuario
        UserUpdateRequest updateRequest = new UserUpdateRequest(
            "demox",
            "demoMod@gmail.com",
            "1234",
            "",
            authenticationResponse.token
        );

        // when
        ResultActions updateRequestResult = mockMvc.perform(put(updateUrl)
            .content(ObjectJson.toJson(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + authenticationResponse.token)
        ).andDo(print());

        // then
        updateRequestResult.andExpect(status().is4xxClientError());
    }
}
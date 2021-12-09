package com.ibank.auth;

import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
import com.ibank.auth.http.TokenValidationRequest;
import com.ibank.common.ErrorResponse;
import com.ibank.user.User;
import com.ibank.user.UserRepository;
import com.ibank.utils.ObjectJson;
import com.ibank.utils.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class AuthenticationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private final String loginUrl = "http://192.168.0.21:8888/api/v1/login";
    private final String tokenValidationUrl = "http://192.168.0.21:8888/api/v1/user/token/validate";

    @BeforeEach
    void setUp() {
        // insertar un usuario en la db
        userRepository.save(
            new User(null, "demo", "demo@gmail.com", PasswordEncoder.encode("demo"))
        );
    }

    // Mejorable: Enviar mensaje de error en la respuesta (esta vacia) no solo el codigo 400
    @Test
    void shouldFailToLoginWhenInvalidForm() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "");

        // when
        ResultActions resultAuthResponse = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );
        resultAuthResponse.andDo(print());

        // then
        resultAuthResponse.andExpect(status().is4xxClientError());
    }

    // Mejorable: Enviar mensaje de error en la respuesta  (Ver campo MockHttpServletResponse -> Error message = null)
    @Test
    void shouldFailToLoginWhenIncorrectCredentials() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo77");

        // when
        ResultActions rt = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        rt.andDo(print());

        // then
        rt.andExpect(status().is4xxClientError());
    }

    @Test
    void shouldLoginWithCorrectCredentials() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo");

        // when
        ResultActions result = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );
        result.andDo(print());

        // La respuesta en json
        String jsonResponse = result.andReturn().getResponse().getContentAsString();

        // Conversion del json a AuthenticationResponse
        AuthenticationResponse authenticationResponse = ObjectJson.fromJson(jsonResponse, AuthenticationResponse.class);

        // then
        result.andExpect(status().is(200));
        assertEquals("demo", authenticationResponse.username);
        assertEquals("demo@gmail.com", authenticationResponse.email);
        log.info(authenticationResponse.toString());
    }

    @Test
    void shouldFailToAccessWhenMissingTokenHeader() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo");
        ResultActions authResult = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // Respuesta en json
        String authJsonResponse = authResult.andReturn().getResponse().getContentAsString();

        // Conversion de la respuesta json a AuthenticationResponse
        AuthenticationResponse authResponse = ObjectJson.fromJson(authJsonResponse, AuthenticationResponse.class);

        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest(
            authResponse.username, authResponse.token
        );

        // when
        ResultActions tokenValidationResult = mockMvc.perform(get(tokenValidationUrl)
            .content(ObjectJson.toJson(tokenValidationRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // Convertimos la respuesta en json
        String tokenValitationJsonResponse = tokenValidationResult.andReturn().getResponse().getContentAsString();

        tokenValidationResult.andDo(print());

        // then
        tokenValidationResult.andExpect(status().is(403));
        ErrorResponse errorResponse = ObjectJson.fromJson(tokenValitationJsonResponse, ErrorResponse.class);
        log.info(errorResponse.errorMessage);
    }

    @Test
    void shouldFailToAccessWhenTokenIsInvalid() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo");
        ResultActions authResult = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // Respuesta en json
        String authJsonResponse = authResult.andReturn().getResponse().getContentAsString();

        // Conversion de la respuesta json a AuthenticationResponse
        AuthenticationResponse authResponse = ObjectJson.fromJson(authJsonResponse, AuthenticationResponse.class);

        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest(
            authResponse.username, authResponse.token
        );

        // when
        ResultActions tokenValidationResult = mockMvc.perform(get(tokenValidationUrl)
            .content(ObjectJson.toJson(tokenValidationRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer invalidtoken")
        );

        tokenValidationResult.andDo(print());

        // then
        tokenValidationResult.andExpect(status().is4xxClientError());
    }

    @Test
    void shouldAccessWhenTokenIsValid() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo");
        ResultActions authResult = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // Respuesta en json
        String authJsonResponse = authResult.andReturn().getResponse().getContentAsString();

        // Conversion de la respuesta json a AuthenticationResponse
        AuthenticationResponse authResponse = ObjectJson.fromJson(authJsonResponse, AuthenticationResponse.class);

        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest(
            authResponse.username, authResponse.token
        );

        // when
        ResultActions tokenValidationResult = mockMvc.perform(get(tokenValidationUrl)
            .content(ObjectJson.toJson(tokenValidationRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + authResponse.token)
        );

        tokenValidationResult.andDo(print());

        // then
        tokenValidationResult.andExpect(status().is(200));
    }

}
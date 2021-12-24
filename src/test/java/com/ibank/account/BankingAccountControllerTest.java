package com.ibank.account;

import com.ibank.account.http.BankingAccountContractRequest;
import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class BankingAccountControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private final String loginUrl = "http://192.168.0.21:8888/api/v1/login";
    private final String accountCreateUrl = "http://192.168.0.21:8888/api/v1/customer/account/create";

    @BeforeEach
    void setUp() {
        // Usuario usado para las pruebas
        userRepository.save(
            new User(null, "demo", "demo@gmail.com", PasswordEncoder.encode("demo"))
        );
    }

    @Test
    void shouldCreateBankingAccount() throws Exception {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo");

        // Resultado de la peticion
        ResultActions authResult = mockMvc.perform(post(loginUrl)
            .content(ObjectJson.toJson(authRequest))
            .contentType(MediaType.APPLICATION_JSON)
        );

        // Obtenemos el json de la respuesta
        String authJsonResponse = authResult.andReturn().getResponse().getContentAsString();

        // Conversion de la respuesta json a AuthenticationResponse
        AuthenticationResponse authResponse = ObjectJson.fromJson(authJsonResponse, AuthenticationResponse.class);

        BankingAccountContractRequest bankingAccountContractRequest = new BankingAccountContractRequest(
            BankingAccountType.SAVINGS, BankingAccountCurrency.EUR
        );

        // when
        ResultActions accountContractResult = mockMvc.perform(post(accountCreateUrl)
            .content(ObjectJson.toJson(bankingAccountContractRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + authResponse.token)
        );

        // then
        accountContractResult.andDo(print()).andExpect(status().is(200));
    }

    @Test
        // Debe fallar al recibir una peticion con datos invalidos
    void shouldFailToCreateBankingAccountWhenInvalidRequest() throws Exception {
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

        String json = """
            {"accountType":"SAVINGS", "accountCurrency":"USDC"}
            """;

        // when
        ResultActions accountContractResult = mockMvc.perform(post(accountCreateUrl)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + authResponse.token)
        );

        // then
        accountContractResult.andDo(print()).andExpect(status().is4xxClientError());
    }
}
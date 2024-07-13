package com.immaculate.carminder.registration;

import com.immaculate.carminder.api.UserAuthenticationController;
import com.immaculate.carminder.api.advice.IllegalStateExceptionAdvice;
import com.immaculate.carminder.core.auth.user.UserAuthenticationService;
import com.immaculate.carminder.core.auth.user.UserRegistrationService;
import com.immaculate.carminder.core.auth.user.requests.UserRegistrationRequest;
import com.immaculate.carminder.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest({UserAuthenticationController.class, IllegalStateExceptionAdvice.class})
public class UserAuthenticationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @SuppressWarnings("unused")
    private UserRegistrationService userRegistrationService;

    @MockBean
    @SuppressWarnings("unused")
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    @SuppressWarnings("unused")
    private WebApplicationContext context;

    @BeforeEach
    public void startServer() {
        mockMvc = webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("should register the user successfully")
    void should_register_the_user_successfully() throws Exception {
        doNothing().when(userRegistrationService).register(any());

        String requestBody = TestUtils.loadResource("src/test/resources/requests/auth/register-user-request.json");

        mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should return 400 when request is invalid")
    void should_return_400_when_request_is_invalid() throws Exception {
        doThrow(new IllegalStateException("Invalid request")).when(userRegistrationService).register(getUserRegistrationRequest());

        String requestBody = TestUtils.loadResource("src/test/resources/requests/auth/invalid-register-user-request.json");

        mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return status 200 and token in body when user logs in successfully")
    void should_return_status_200_and_token_in_body_when_user_logs_in_successfully() throws Exception {
        when(userAuthenticationService.login(any())).thenReturn("JwtToken");

        String requestBody = TestUtils.loadResource("src/test/resources/requests/auth/login-user-request.json");

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("JwtToken"))
                .andExpect(jsonPath("$.type").value("Bearer "));
    }

    private static UserRegistrationRequest getUserRegistrationRequest() {
        return UserRegistrationRequest.builder()
                .name("testName")
                .password("pass123")
                .email("invalid")
                .build();
    }
}
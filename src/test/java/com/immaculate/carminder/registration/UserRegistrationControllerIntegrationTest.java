package com.immaculate.carminder.registration;

import com.immaculate.carminder.api.UserRegistrationController;
import com.immaculate.carminder.api.advice.IllegalStateExceptionAdvice;
import com.immaculate.carminder.core.auth.user.UserRegistrationRequest;
import com.immaculate.carminder.core.auth.user.UserRegistrationService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest({UserRegistrationController.class, IllegalStateExceptionAdvice.class})
public class UserRegistrationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @SuppressWarnings("unused")
    private UserRegistrationService userRegistrationService;

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

        String requestBody = TestUtils.loadResource("src/test/resources/requests/register-user-request.json");

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

        String requestBody = TestUtils.loadResource("src/test/resources/requests/invalid-register-user-request.json");

        mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andExpect(status().isBadRequest());
    }

    private static UserRegistrationRequest getUserRegistrationRequest() {
        return UserRegistrationRequest.builder()
                .name("testName")
                .password("pass123")
                .email("invalid")
                .build();
    }
}
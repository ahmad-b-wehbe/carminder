package com.immaculate.carminder.registration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class RegistrationControllerIntegrationTest {
    private ClientAndServer mockServer;
    private MockServerClient client;
    private RestTemplate restTemplate;

    @BeforeEach
    public void startServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);
        client = new MockServerClient("localhost", 1080);
        restTemplate = new RestTemplate();
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    @DisplayName("should register the user successfully")
    void should_register_the_user_successfully() {
        String url = "http://localhost:1080/api/v1/registration";
        String success_message = "Registration successful";

        client.when(expectedRequest()).respond(expectedResponse());

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                getHttpEntity(actualRequest()),
                String.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(success_message, responseEntity.getBody());
        client.verify(expectedRequest());
    }

    private static HttpEntity<RegistrationRequest> getHttpEntity(RegistrationRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(request, headers);
    }

    private static RegistrationRequest actualRequest() {
        return RegistrationRequest.builder()
                .name("ahmad")
                .email("ahmad@gmail.com")
                .password("pass123")
                .build();
    }

    private static HttpResponse expectedResponse() {
        return response()
                .withStatusCode(200)
                .withBody("Registration successful");
    }

    private static org.mockserver.model.HttpRequest expectedRequest() {
        return request()
                .withMethod("POST")
                .withPath("/api/v1/registration");
    }
}
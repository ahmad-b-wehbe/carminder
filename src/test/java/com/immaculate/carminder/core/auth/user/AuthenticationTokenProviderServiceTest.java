package com.immaculate.carminder.core.auth.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationTokenProviderServiceTest {
    private AuthenticationTokenProviderService authenticationTokenProviderService;

    @BeforeEach
    void setUp() {
        authenticationTokenProviderService = new AuthenticationTokenProviderService();
    }

    @Test
    @DisplayName("should return username password authentication token given user details")
    void should_return_username_password_authentication_token_given_user_details() {
        String username = "username";
        String password = "password";
        UsernamePasswordAuthenticationToken authentication = authenticationTokenProviderService.generateToken(username, password);
        assertEquals(username, authentication.getName());
        assertEquals(password, authentication.getCredentials());
    }
}
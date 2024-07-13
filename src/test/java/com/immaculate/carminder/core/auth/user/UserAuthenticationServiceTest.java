package com.immaculate.carminder.core.auth.user;

import com.immaculate.carminder.core.auth.JwtGenerator;
import com.immaculate.carminder.core.auth.user.requests.UserLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserAuthenticationServiceTest {
    private static final String EMAIL = "email";
    private static final String PASS = "pass";
    private UserAuthenticationService userAuthenticationService;
    private AuthenticationManager authenticationManager;
    private AuthenticationTokenProviderService authenticationTokenProviderService;
    private JwtGenerator jwtGenerator;

    @BeforeEach
    void setUp() {
        jwtGenerator = mock(JwtGenerator.class);
        authenticationManager = mock(AuthenticationManager.class);
        authenticationTokenProviderService = mock(AuthenticationTokenProviderService.class);
        userAuthenticationService = new UserAuthenticationService(jwtGenerator, authenticationManager, authenticationTokenProviderService);
    }

    @Test
    @DisplayName("should set the security context in the app when the user provides correct credentials")
    void should_set_the_security_context_in_the_app_when_the_user_provides_correct_credentials() {
        Authentication authentication = getAuthentication();

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            userAuthenticationService.login(UserLoginRequest.builder().email(EMAIL).password(PASS).build());

            verify(securityContext).setAuthentication(authentication);
        }
    }

    @Test
    @DisplayName("should return the jwt token when user signs in with correct credentials")
    void should_return_the_jwt_token_when_user_signs_in_with_correct_credentials() {
        Authentication authentication = getAuthentication();
        String jwtToken = "jwtToken";
        when(jwtGenerator.generateToken(authentication)).thenReturn(jwtToken);
        String generatedToken = userAuthenticationService.login(UserLoginRequest.builder().email(EMAIL).password(PASS).build());
        assertEquals(jwtToken, generatedToken);
    }

    private Authentication getAuthentication() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationTokenProviderService.generateToken(EMAIL, PASS)).thenReturn(usernamePasswordAuthToken);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(usernamePasswordAuthToken)).thenReturn(authentication);
        return authentication;
    }
}
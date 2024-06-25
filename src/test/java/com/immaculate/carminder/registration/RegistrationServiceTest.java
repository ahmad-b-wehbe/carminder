package com.immaculate.carminder.registration;

import com.immaculate.carminder.appuser.AppUser;
import com.immaculate.carminder.appuser.AppUserService;
import com.immaculate.carminder.registration.token.ConfirmationToken;
import com.immaculate.carminder.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {
    private RegistrationService registrationService;
    private EmailValidator emailValidator;
    private AppUserService appUserService;
    private ConfirmationTokenService confirmationTokenService;

    @BeforeEach
    void setUp() {
        emailValidator = mock(EmailValidator.class);
        appUserService = mock(AppUserService.class);
        confirmationTokenService = mock(ConfirmationTokenService.class);
        registrationService = new RegistrationService(emailValidator, appUserService, confirmationTokenService);
    }

    @Test
    @DisplayName("should throw an exception if email is not valid")
    void should_throw_an_exception_if_email_is_not_valid() {
        String invalidEmail = "invalidEmail";
        when(emailValidator.test(invalidEmail)).thenReturn(false);
        RegistrationRequest request = mock(RegistrationRequest.class);
        when(request.email()).thenReturn(invalidEmail);
        assertThrows(IllegalStateException.class, () -> registrationService.register(request));
    }

    @Test
    @DisplayName("should signup user if email is valid")
    void should_signup_user_if_email_is_valid() {
        String validEmail = "valid@gmail.com";
        when(emailValidator.test(validEmail)).thenReturn(true);
        RegistrationRequest request = mock(RegistrationRequest.class);
        when(request.email()).thenReturn(validEmail);
        registrationService.register(request);
        verify(appUserService).signUpUser(any(AppUser.class));
    }

    @Test
    @DisplayName("should throw an exception if token is not found")
    void should_throw_an_exception_if_token_is_not_found() {
        String token = "token";
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> registrationService.confirmToken(token));
    }


    @Test
    @DisplayName("should throw an exception if email is already confirmed")
    void should_throw_an_exception_if_email_is_already_confirmed() {
        String token = "token";
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getConfirmedAt()).thenReturn(LocalDateTime.now());
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));
        assertThrows(IllegalStateException.class, () -> registrationService.confirmToken(token));
    }

    @Test
    @DisplayName("should throw an exception if token is expired")
    void should_throw_an_exception_if_token_is_expired() {
        String token = "token";
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getConfirmedAt()).thenReturn(null);
        when(confirmationToken.getExpiresAt()).thenReturn(LocalDateTime.now().minusMinutes(1));
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));
        assertThrows(IllegalStateException.class, () -> registrationService.confirmToken(token));
    }


    @Test
    @DisplayName("should confirm token")
    void should_confirm_token() {
        String token = "token";
        ConfirmationToken confirmationToken = mock(ConfirmationToken.class);
        when(confirmationToken.getConfirmedAt()).thenReturn(null);
        when(confirmationToken.getExpiresAt()).thenReturn(LocalDateTime.now().plusMinutes(1));
        AppUser appUser = mock(AppUser.class);
        when(confirmationToken.getAppUser()).thenReturn(appUser);
        String email = "email";
        when(appUser.getEmail()).thenReturn(email);
        when(confirmationTokenService.getToken(token)).thenReturn(Optional.of(confirmationToken));
        registrationService.confirmToken(token);
        verify(confirmationTokenService).setConfirmedAt(token);
        verify(appUserService).enableAppUser(email);
    }
}
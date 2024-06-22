package com.immaculate.carminder.registration;

import com.immaculate.carminder.appuser.AppUser;
import com.immaculate.carminder.appuser.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {
    private RegistrationService registrationService;
    private EmailValidator emailValidator;
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        emailValidator = mock(EmailValidator.class);
        appUserService = mock(AppUserService.class);
        registrationService = new RegistrationService(emailValidator, appUserService);
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
}
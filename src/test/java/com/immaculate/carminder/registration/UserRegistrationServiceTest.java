package com.immaculate.carminder.registration;

import com.immaculate.carminder.core.auth.user.AppUser;
import com.immaculate.carminder.core.auth.user.AppUserPersistenceManager;
import com.immaculate.carminder.core.auth.user.UserRegistrationRequest;
import com.immaculate.carminder.core.auth.user.UserRegistrationService;
import com.immaculate.carminder.core.auth.validators.EmailValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserRegistrationServiceTest {
    private UserRegistrationService userRegistrationService;
    private EmailValidationService emailValidationService;
    private AppUserPersistenceManager appUserPersistenceManager;

    @BeforeEach
    void setUp() {
        emailValidationService = mock(EmailValidationService.class);
        appUserPersistenceManager = mock(AppUserPersistenceManager.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        userRegistrationService = new UserRegistrationService(emailValidationService, appUserPersistenceManager, bCryptPasswordEncoder);
    }

    @Test
    @DisplayName("should throw an exception if email is not valid")
    void should_throw_an_exception_if_email_is_not_valid() {
        String invalidEmail = "invalidEmail";
        when(emailValidationService.test(invalidEmail)).thenReturn(false);
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        when(request.email()).thenReturn(invalidEmail);
        assertThrows(IllegalStateException.class, () -> userRegistrationService.register(request));
    }

    @Test
    @DisplayName("should throw an exception if email is already taken")
    void should_throw_an_exception_if_email_is_already_taken() {
        String email = "takenEmail";
        when(emailValidationService.test(email)).thenReturn(true);
        Optional<AppUser> existingUser = Optional.of(mock(AppUser.class));
        when(appUserPersistenceManager.findByEmail(email)).thenReturn(existingUser);
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        when(request.email()).thenReturn(email);
        assertThrows(IllegalStateException.class, () -> userRegistrationService.register(request));
    }

    @Test
    @DisplayName("should register user if email is valid and not taken")
    void should_register_user_if_email_is_valid_and_not_taken() {
        String email = "validEmail";
        when(emailValidationService.test(email)).thenReturn(true);
        when(appUserPersistenceManager.findByEmail(email)).thenReturn(Optional.empty());
        UserRegistrationRequest request = mock(UserRegistrationRequest.class);
        when(request.email()).thenReturn(email);
        userRegistrationService.register(request);
        verify(appUserPersistenceManager).save(any(AppUser.class));
    }
}
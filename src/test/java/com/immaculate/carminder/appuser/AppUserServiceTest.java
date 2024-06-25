package com.immaculate.carminder.appuser;

import com.immaculate.carminder.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AppUserServiceTest {
    private UserRepository userRepository;
    private AppUserService appUserService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenService confirmationTokenService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        confirmationTokenService = mock(ConfirmationTokenService.class);
        appUserService = new AppUserService(userRepository, bCryptPasswordEncoder, confirmationTokenService);
    }

    @Test
    @DisplayName("should find user by username when email exists")
    void should_find_user_by_username_when_email_exists() {
        String email = "email";
        Optional<AppUser> appUser = Optional.of(mock(AppUser.class));
        when(userRepository.findByEmail(email)).thenReturn(appUser);
        assertEquals(appUser.get(), appUserService.loadUserByUsername(email));
    }

    @Test
    @DisplayName("should throw username not found exception when email does not exist")
    void should_throw_username_not_found_exception_when_email_does_not_exist() {
        String email = "email";
        when(userRepository.findByEmail(email)).thenThrow(new UsernameNotFoundException("username does not exist"));
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername(email));
    }

    @Test
    @DisplayName("should throw an exception if email is already taken")
    void should_throw_an_exception_if_email_is_already_taken() {
        String email = "email";
        AppUser appUser = mock(AppUser.class);
        when(appUser.getEmail()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(appUser));
        assertThrows(IllegalStateException.class, () -> appUserService.signUpUser(appUser));
    }

    @Test
    @DisplayName("should save user if email is not taken")
    void should_save_user_if_email_is_not_taken() {
        String email = "email";
        AppUser appUser = mock(AppUser.class);
        when(appUser.getEmail()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(appUser.getPassword())).thenReturn("encodedPassword");
        appUserService.signUpUser(appUser);
        verify(userRepository).save(appUser);
    }

    @Test
    @DisplayName("should save confirmation token")
    void should_save_confirmation_token() {
        String email = "email";
        AppUser appUser = mock(AppUser.class);
        when(appUser.getEmail()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(appUser.getPassword())).thenReturn("encodedPassword");
        appUserService.signUpUser(appUser);
        verify(confirmationTokenService).saveConfirmationToken(any());
    }

    @Test
    @DisplayName("should enable user")
    void should_enable_user() {
        String email = "email";
        when(userRepository.enableAppUser(email)).thenReturn(1);
        assertEquals(1, appUserService.enableAppUser(email));
    }
}
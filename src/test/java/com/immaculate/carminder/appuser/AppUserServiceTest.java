package com.immaculate.carminder.appuser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppUserServiceTest {
    private UserRepository userRepository;
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        appUserService = new AppUserService(userRepository);
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
}
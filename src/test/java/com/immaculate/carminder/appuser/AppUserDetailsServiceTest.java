package com.immaculate.carminder.appuser;

import com.immaculate.carminder.infra.auth.user.AppUserDetailsService;
import com.immaculate.carminder.infra.auth.user.entity.AppUserEntity;
import com.immaculate.carminder.infra.auth.user.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppUserDetailsServiceTest {
    private AppUserRepository appUserRepository;
    private AppUserDetailsService appUserDetailsService;

    @BeforeEach
    void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        appUserDetailsService = new AppUserDetailsService(appUserRepository);
    }

    @Test
    @DisplayName("should find user by username when email exists")
    void should_find_user_by_username_when_email_exists() {
        String email = "email";
        Optional<AppUserEntity> appUser = Optional.of(mock(AppUserEntity.class));
        when(appUserRepository.findByEmail(email)).thenReturn(appUser);
        assertEquals(appUser.get(), appUserDetailsService.loadUserByUsername(email));
    }

    @Test
    @DisplayName("should throw username not found exception when email does not exist")
    void should_throw_username_not_found_exception_when_email_does_not_exist() {
        String email = "email";
        when(appUserRepository.findByEmail(email)).thenThrow(new UsernameNotFoundException("username does not exist"));
        assertThrows(UsernameNotFoundException.class, () -> appUserDetailsService.loadUserByUsername(email));
    }
}
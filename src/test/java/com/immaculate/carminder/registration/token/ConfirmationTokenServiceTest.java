package com.immaculate.carminder.registration.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConfirmationTokenServiceTest {
    private ConfirmationTokenService confirmationTokenService;
    private ConfirmationTokenRepository confirmationTokenRepository;

    @BeforeEach
    void setUp() {
        confirmationTokenRepository = mock(ConfirmationTokenRepository.class);
        confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository);
    }

    @Test
    @DisplayName("should save confirmation token")
    void should_save_confirmation_token() {
        ConfirmationToken token = mock(ConfirmationToken.class);
        confirmationTokenService.saveConfirmationToken(token);
        verify(confirmationTokenRepository).save(token);
    }

    @Test
    @DisplayName("should get confirmation token by token")
    void should_get_confirmation_token_by_token() {
        String token = "token";
        Optional<ConfirmationToken> confirmationToken = Optional.of(mock(ConfirmationToken.class));
        when(confirmationTokenRepository.findByToken(token)).thenReturn(confirmationToken);
        assertEquals(confirmationToken, confirmationTokenService.getToken(token));
    }

    @Test
    @DisplayName("should set confirmed using a token")
    void should_set_confirmed_using_a_token() {
        String token = "token";
        when(confirmationTokenRepository.updateConfirmedAt(eq(token), any())).thenReturn(1);
        assertEquals(1, confirmationTokenService.setConfirmedAt(token));
    }
}
package com.immaculate.carminder.registration.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
}
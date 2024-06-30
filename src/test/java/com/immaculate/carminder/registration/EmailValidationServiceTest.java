package com.immaculate.carminder.registration;

import com.immaculate.carminder.core.auth.validators.EmailValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailValidationServiceTest {
    private EmailValidationService emailValidationService;

    @BeforeEach
    void setUp() {
        emailValidationService = new EmailValidationService();
    }

    @Test
    @DisplayName("should validate correct email")
    void should_validate_correct_email() {
        String email = "test@example.com";
        boolean isValid = emailValidationService.test(email);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("should invalidate email without at symbol")
    void should_invalidate_email_without_at_symbol() {
        String email = "testexample.com";
        boolean isValid = emailValidationService.test(email);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("should invalidate email without domain")
    void should_invalidate_email_without_domain() {
        String email = "test@";
        boolean isValid = emailValidationService.test(email);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("should invalidate email without username")
    void should_invalidate_email_without_username() {
        String email = "@example.com";
        boolean isValid = emailValidationService.test(email);
        assertFalse(isValid);
    }
}
package com.immaculate.carminder.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistrationServiceTest {
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService();
    }

    @Test
    @DisplayName("should return works whenever it is called")
    void should_return_works_whenever_it_is_called() {
        assertEquals("works", registrationService.register(RegistrationRequest.builder().build()));
    }
}
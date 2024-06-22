package com.immaculate.carminder.registration;

import lombok.Builder;

@Builder
public record RegistrationRequest(String name, String email, String password) {
}

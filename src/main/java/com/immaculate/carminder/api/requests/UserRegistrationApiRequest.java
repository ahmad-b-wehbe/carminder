package com.immaculate.carminder.api.requests;

import lombok.Builder;

@Builder
public record UserRegistrationApiRequest(String name, String email, String password) {
}

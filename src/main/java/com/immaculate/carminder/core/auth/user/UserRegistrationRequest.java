package com.immaculate.carminder.core.auth.user;

import lombok.Builder;

@Builder
public record UserRegistrationRequest(String name, String email, String password) {
}

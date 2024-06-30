package com.immaculate.carminder.api.mapper;

import com.immaculate.carminder.api.requests.UserRegistrationApiRequest;
import com.immaculate.carminder.core.auth.user.UserRegistrationRequest;

public class UserRegistrationRequestMapper {
    public static UserRegistrationRequest mapToDomain(UserRegistrationApiRequest request) {
        return UserRegistrationRequest.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }
}

package com.immaculate.carminder.api.mapper;

import com.immaculate.carminder.api.requests.UserLoginApiRequest;
import com.immaculate.carminder.core.auth.user.requests.UserLoginRequest;

public class UserLoginRequestMapper {
    public static UserLoginRequest from(UserLoginApiRequest request) {
        return UserLoginRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}

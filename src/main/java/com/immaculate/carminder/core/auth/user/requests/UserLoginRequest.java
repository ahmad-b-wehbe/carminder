package com.immaculate.carminder.core.auth.user.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRequest {
    private String email;
    private String password;
}

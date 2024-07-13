package com.immaculate.carminder.api.requests;

import lombok.Data;

@Data
public class UserLoginApiRequest {
    private String email;
    private String password;
}

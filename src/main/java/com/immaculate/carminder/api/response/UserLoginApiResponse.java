package com.immaculate.carminder.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginApiResponse {
    private String token;
    private final String type = "Bearer ";
}

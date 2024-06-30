package com.immaculate.carminder.core.auth.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUser {
    private String name;
    private String email;
    private String password;
    private AppUserRole role;
}

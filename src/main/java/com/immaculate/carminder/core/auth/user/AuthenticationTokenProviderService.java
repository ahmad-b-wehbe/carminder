package com.immaculate.carminder.core.auth.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationTokenProviderService {
    public UsernamePasswordAuthenticationToken generateToken(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}

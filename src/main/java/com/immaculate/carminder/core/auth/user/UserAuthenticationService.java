package com.immaculate.carminder.core.auth.user;

import com.immaculate.carminder.core.auth.JwtGenerator;
import com.immaculate.carminder.core.auth.user.requests.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {
    private final JwtGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationTokenProviderService authenticationTokenProviderService;

    public String login(UserLoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = authenticationTokenProviderService.generateToken(request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtGenerator.generateToken(authentication);
    }
}

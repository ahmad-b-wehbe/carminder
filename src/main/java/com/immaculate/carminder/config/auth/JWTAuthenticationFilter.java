package com.immaculate.carminder.config.auth;

import ch.qos.logback.core.util.StringUtil;
import com.immaculate.carminder.core.auth.JwtGenerator;
import com.immaculate.carminder.infra.auth.user.AppUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String BEARER_KEY = "Bearer ";
    private final JwtGenerator jwtGenerator;
    private final AppUserDetailsService appUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        if (StringUtil.notNullNorEmpty(token) && jwtGenerator.validateToken(token)) {
            String username = jwtGenerator.getUsernameFromJwt(token);
            UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
            WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
            authenticationToken.setDetails(details);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER_KEY);
        if (StringUtil.notNullNorEmpty(header) && header.startsWith(BEARER_KEY)) {
            return header.substring(7);
        }
        return null;
    }
}

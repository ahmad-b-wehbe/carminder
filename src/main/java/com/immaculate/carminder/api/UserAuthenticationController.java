package com.immaculate.carminder.api;

import com.immaculate.carminder.api.mapper.UserRegistrationRequestMapper;
import com.immaculate.carminder.api.requests.UserLoginApiRequest;
import com.immaculate.carminder.api.requests.UserRegistrationApiRequest;
import com.immaculate.carminder.api.response.UserLoginApiResponse;
import com.immaculate.carminder.core.auth.user.UserAuthenticationService;
import com.immaculate.carminder.core.auth.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.immaculate.carminder.api.mapper.UserLoginRequestMapper.from;

@RestController
@RequestMapping(path = "api/v1/")
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationController {
    private final UserRegistrationService userRegistrationService;
    private final UserAuthenticationService userAuthenticationService;

    @PostMapping("registration")
    public ResponseEntity<Void> register(@RequestBody UserRegistrationApiRequest request) {
        userRegistrationService.register(UserRegistrationRequestMapper.mapToDomain(request));
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity<UserLoginApiResponse> login(@RequestBody UserLoginApiRequest request) {
        String token = userAuthenticationService.login(from(request));
        return ResponseEntity.ok().body(UserLoginApiResponse.builder().token(token).build());
    }

}

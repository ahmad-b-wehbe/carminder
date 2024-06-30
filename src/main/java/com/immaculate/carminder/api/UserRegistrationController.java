package com.immaculate.carminder.api;

import com.immaculate.carminder.api.mapper.UserRegistrationRequestMapper;
import com.immaculate.carminder.api.requests.UserRegistrationApiRequest;
import com.immaculate.carminder.core.auth.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/registration")
@RequiredArgsConstructor
public class UserRegistrationController {
    private final UserRegistrationService userRegistrationService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody UserRegistrationApiRequest request) {
        userRegistrationService.register(UserRegistrationRequestMapper.mapToDomain(request));
        return ResponseEntity.ok().build();
    }

}

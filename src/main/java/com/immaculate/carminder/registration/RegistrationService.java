package com.immaculate.carminder.registration;

import com.immaculate.carminder.appuser.AppUser;
import com.immaculate.carminder.appuser.AppUserRole;
import com.immaculate.carminder.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.email());
        if (!isValidEmail) throw new IllegalStateException("email not valid");
        return appUserService.signUpUser(getUser(request));
    }

    private static AppUser getUser(RegistrationRequest request) {
        return new AppUser(
                request.name(),
                request.email(),
                request.password(),
                AppUserRole.USER
        );
    }
}

package com.immaculate.carminder.core.auth.user;

import com.immaculate.carminder.core.auth.user.requests.UserRegistrationRequest;
import com.immaculate.carminder.core.auth.validators.EmailValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final EmailValidationService emailValidationService;
    private final AppUserPersistenceManager appUserPersistenceManager;
    private final PasswordEncoder bCryptPasswordEncoder;

    public void register(UserRegistrationRequest request) {
        validateEmailFormat(request.email());
        validateEmailIsUnique(request.email());
        String encodedPassword = bCryptPasswordEncoder.encode(request.password());
        AppUser appUser = getUser(request);
        appUser.setPassword(encodedPassword);
        appUserPersistenceManager.save(appUser);
    }

    private void validateEmailIsUnique(String email) {
        boolean userExists = appUserPersistenceManager.findByEmail(email).isPresent();
        if (userExists)
            throw new IllegalStateException("Email is already taken, please choose another one or login if you already have an account");
    }

    private void validateEmailFormat(String email) {
        boolean isValidEmail = emailValidationService.test(email);
        if (!isValidEmail)
            throw new IllegalStateException("Please enter a valid email address");
    }

    private static AppUser getUser(UserRegistrationRequest request) {
        return new AppUser(
                request.name(),
                request.email(),
                request.password(),
                AppUserRole.USER
        );
    }
}

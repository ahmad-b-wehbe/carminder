package com.immaculate.carminder.registration;

import com.immaculate.carminder.appuser.AppUser;
import com.immaculate.carminder.appuser.AppUserRole;
import com.immaculate.carminder.appuser.AppUserService;
import com.immaculate.carminder.registration.token.ConfirmationToken;
import com.immaculate.carminder.registration.token.ConfirmationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.email());
        if (!isValidEmail) throw new IllegalStateException("email not valid");
        return appUserService.signUpUser(getUser(request));
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "confirmed";
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

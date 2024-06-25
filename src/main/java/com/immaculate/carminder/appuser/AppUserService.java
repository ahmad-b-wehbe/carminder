package com.immaculate.carminder.appuser;

import com.immaculate.carminder.registration.token.ConfirmationToken;
import com.immaculate.carminder.registration.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private static final String USER_NOT_FOUND = "User with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = userRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if (userExists) throw new IllegalStateException("email already taken");
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        userRepository.save(appUser);
        String token = UUID.randomUUID().toString();
        confirmationTokenService.saveConfirmationToken(getConfirmationToken(appUser, token));
        //TODO: send email
        return token;
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    private static ConfirmationToken getConfirmationToken(AppUser appUser, String token) {
        return ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .appUser(appUser)
                .build();
    }
}

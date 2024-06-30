package com.immaculate.carminder.infra.auth.user;

import com.immaculate.carminder.core.auth.user.AppUser;
import com.immaculate.carminder.core.auth.user.AppUserPersistenceManager;
import com.immaculate.carminder.infra.auth.user.mapper.AppUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppUserJpaPersistenceManager implements AppUserPersistenceManager {
    private final AppUserRepository appUserRepository;

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email).map(AppUserMapper::mapToDomain);
    }

    @Override
    public void save(AppUser appUser) {
        appUserRepository.save(AppUserMapper.mapToEntity(appUser));
    }
}

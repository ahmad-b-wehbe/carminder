package com.immaculate.carminder.core.auth.user;

import java.util.Optional;

public interface AppUserPersistenceManager {
    Optional<AppUser> findByEmail(String email);

    void save(AppUser appUser);
}

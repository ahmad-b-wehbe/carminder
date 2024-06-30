package com.immaculate.carminder.infra.auth.user.mapper;

import com.immaculate.carminder.core.auth.user.AppUser;
import com.immaculate.carminder.infra.auth.user.entity.AppUserEntity;

public class AppUserMapper {
    public static AppUser mapToDomain(AppUserEntity entity) {
        return AppUser.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }

    public static AppUserEntity mapToEntity(AppUser domain) {
        return new AppUserEntity(
                domain.getName(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getRole()
        );
    }
}

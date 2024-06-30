package com.immaculate.carminder.infra.auth.user;

import com.immaculate.carminder.core.auth.user.AppUser;
import com.immaculate.carminder.infra.auth.user.entity.AppUserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AppUserJpaPersistenceManager.class})
public class AppUserJpaPersistenceManagerIntegrationTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    @SuppressWarnings("unused")
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    @SuppressWarnings("unused")
    private AppUserJpaPersistenceManager appUserJpaPersistenceManager;

    @Autowired
    @SuppressWarnings("unused")
    private AppUserRepository appUserRepository;

    @Test
    @DisplayName("should save user to the database")
    void should_save_user_to_the_database() {
        AppUser testUser = AppUser.builder().name("testName").email("test@example.com").password("pass123").build();
        appUserJpaPersistenceManager.save(testUser);
        Optional<AppUserEntity> retrievedUser = appUserRepository.findByEmail(testUser.getEmail());
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo(testUser.getEmail());
        assertThat(retrievedUser.get().getName()).isEqualTo(testUser.getName());
    }

    @Test
    @DisplayName("should find user by email from database")
    void should_find_user_by_email_from_database() {
        AppUserEntity testUserEntity = new AppUserEntity();
        testUserEntity.setEmail("existinguser@example.com");
        testUserEntity.setName("Existing User");
        appUserRepository.save(testUserEntity);
        Optional<AppUser> retrievedUser = appUserJpaPersistenceManager.findByEmail(testUserEntity.getEmail());
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo(testUserEntity.getEmail());
        assertThat(retrievedUser.get().getName()).isEqualTo(testUserEntity.getName());
    }

}

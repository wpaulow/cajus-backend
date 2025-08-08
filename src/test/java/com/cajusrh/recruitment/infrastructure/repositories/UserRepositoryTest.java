package com.cajusrh.recruitment.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.cajusrh.recruitment.core.domain.enums.UserRole;
import com.cajusrh.recruitment.core.domain.enums.UserStatus;
import com.cajusrh.recruitment.infrastructure.entities.UserEntity;

@DataJpaTest
@ActiveProfiles("test") 
public class UserRepositoryTest {
	@Autowired
    private SpringUserJpaRepository repo;

    @Test
    void saveAndFindByEmail() {
        UserEntity e = new UserEntity(
            "emailmaluco@maluco.com", 
            "hashMaluco",
            UserRole.CANDIDATE, 
            UserStatus.ACTIVE,
            Instant.now(), 
            Instant.now()
        );
        repo.save(e);
        var found = repo.findByEmail("emailmaluco@maluco.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("emailmaluco@maluco.com");
    }
}

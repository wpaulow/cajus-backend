package com.cajusrh.recruitment.infrastructure.repositories;

import com.cajusrh.recruitment.infrastructure.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringUserJpaRepository extends JpaRepository<UserEntity, UUID> {
	Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}

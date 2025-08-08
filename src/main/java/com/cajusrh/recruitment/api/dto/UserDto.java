package com.cajusrh.recruitment.api.dto;

import com.cajusrh.recruitment.core.domain.enums.UserRole;
import com.cajusrh.recruitment.core.domain.enums.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    String email,
    UserRole role,
    UserStatus status,
    Instant createdAt,
    Instant updatedAt
) {}

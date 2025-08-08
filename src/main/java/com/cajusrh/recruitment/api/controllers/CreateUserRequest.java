package com.cajusrh.recruitment.api.controllers;

import com.cajusrh.recruitment.core.domain.enums.UserRole;

public record CreateUserRequest(
	String email,
    String password,
    UserRole role
) {}

package com.cajusrh.recruitment.api.controllers;

import com.cajusrh.recruitment.core.domain.enums.UserStatus;

public record UpdateStatusRequest(
    UserStatus status
) {}
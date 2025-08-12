package com.cajusrh.recruitment.api.mappers;

import org.springframework.stereotype.Component;

import com.cajusrh.recruitment.api.dto.UserDto;
import com.cajusrh.recruitment.core.domain.entities.User;

@Component
public class UserDtoMapper {
    public UserDto toDto(User u) {
        return new UserDto(
            u.getId().getValue(),
            u.getEmail().getValue(),
            u.getRole(),
            u.getStatus(),
            u.getCreatedAt(),
            u.getUpdatedAt()
        );
    }
}
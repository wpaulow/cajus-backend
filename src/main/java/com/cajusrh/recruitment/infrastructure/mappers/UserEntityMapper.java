package com.cajusrh.recruitment.infrastructure.mappers;

import com.cajusrh.recruitment.core.domain.entities.User;
import com.cajusrh.recruitment.core.domain.valueobjects.Email;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;
import com.cajusrh.recruitment.infrastructure.entities.UserEntity;

import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
	
	public UserEntity toEntity(User user) {
		if (user == null) {
            return null;
        }
		return new UserEntity(
                user.getEmail().getValue(),
                user.getPasswordHash(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
	
	public User toDomain(UserEntity entity) {
		if (entity == null) {
            return null;
        }
        return User.load(
            UserId.of(entity.getId()),
            new Email(entity.getEmail()),
            entity.getPasswordHash(),
            entity.getRole(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}

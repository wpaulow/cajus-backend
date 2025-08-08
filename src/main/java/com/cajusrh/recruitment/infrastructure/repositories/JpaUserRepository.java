package com.cajusrh.recruitment.infrastructure.repositories;

import com.cajusrh.recruitment.core.domain.entities.User;
import com.cajusrh.recruitment.core.domain.valueobjects.Email;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;
import com.cajusrh.recruitment.core.ports.out.UserRepositoryPort;
import com.cajusrh.recruitment.infrastructure.entities.UserEntity;
import com.cajusrh.recruitment.infrastructure.mappers.UserEntityMapper;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepositoryPort{
	private final SpringUserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    public JpaUserRepository(SpringUserJpaRepository jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaRepository.findById(userId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }

	@Override
	public void deleteById(UserId id) {
		jpaRepository.deleteById(id.getValue());
		
	}
}

package com.cajusrh.recruitment.core.ports.out;

import com.cajusrh.recruitment.core.domain.entities.User;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;
import com.cajusrh.recruitment.core.domain.valueobjects.Email;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    void deleteById(UserId id); // It may be unnecessary, as the user will be active or inactive
}
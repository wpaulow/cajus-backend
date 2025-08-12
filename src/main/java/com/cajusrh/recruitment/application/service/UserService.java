package com.cajusrh.recruitment.application.service;

import com.cajusrh.recruitment.core.domain.entities.User;
import com.cajusrh.recruitment.core.domain.enums.UserRole;
import com.cajusrh.recruitment.core.domain.enums.UserStatus;
import com.cajusrh.recruitment.core.domain.valueobjects.Email;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;
import com.cajusrh.recruitment.core.ports.out.UserRepositoryPort;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
	private final UserRepositoryPort userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepositoryPort repository, PasswordEncoder passwordEncoder) {
		this.userRepository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User createUser(Email email, String rawPassword, UserRole role) {
//        Email emailVO = new Email(email);
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = User.create(email, hashedPassword, role);
        
        return userRepository.save(user);
    }
	
	// convenience for the controller
//	public User createUser(String email, String rawPassword, UserRole role) {
//	    return createUser(new Email(email), rawPassword, role);
//	}
	
	public User updateUserStatus(UserId userId, UserStatus newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        user.updateStatus(newStatus);
        return userRepository.save(user);
    }
	
	public User updateUserPassword(UserId userId, String newRawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        String hashedPassword = passwordEncoder.encode(newRawPassword);
        user.updatePassword(hashedPassword);
        
        return userRepository.save(user);
    }
	
	public void deleteUser(UserId userId) {
//        if (!userRepository.findById(userId).isPresent()) { // Trip 1 to DB
//            throw new IllegalArgumentException("User not found with ID: " + userId);
//        }
        userRepository.deleteById(userId); // Trip 2 to DB
    }
	
	@Transactional(readOnly = true)
    public Optional<User> findById(UserId userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(Email email) {
//        Email emailVO = new Email(email);
        return userRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public boolean existsByEmail(Email email) {
//        Email emailVO = new Email(email);
        return userRepository.existsByEmail(email);
    }
}

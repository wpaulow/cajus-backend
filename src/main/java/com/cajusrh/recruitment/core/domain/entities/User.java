package com.cajusrh.recruitment.core.domain.entities;

import com.cajusrh.recruitment.core.domain.enums.UserRole;
import com.cajusrh.recruitment.core.domain.enums.UserStatus;
import com.cajusrh.recruitment.core.domain.valueobjects.Email;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;

import java.time.Instant;
import java.util.Objects;

public class User {
	private final UserId id;
    private final Email email;
    private String passwordHash;
    private final UserRole role;
    private UserStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    
    private User(UserId id, Email email, String passwordHash, UserRole role, 
		UserStatus status, Instant createdAt, Instant updatedAt) {
    	this.id = Objects.requireNonNull(id, "UserId is required");
        this.email = Objects.requireNonNull(email, "Email is required");
        this.passwordHash = validatePasswordHash(passwordHash);
        this.role = Objects.requireNonNull(role, "UserRole is required");
        this.status = Objects.requireNonNull(status, "UserStatus is required");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "UpdatedAt is required");
}
    
//   Factory method for create new users
    public static User create(Email email, String passwordHash, UserRole role) {
    	Instant now = Instant.now();
    	return new User(
    		UserId.generate(),
    		email,
    		passwordHash,
    		role,
    		UserStatus.ACTIVE,
            now,
            now
    	);
    }
    
//    Factory method for reconstructing from persistence, 
//    something like: public static User fromDto() or just load()
    public static User load(
        UserId id,
        Email email,
        String passwordHash,
        UserRole role,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
    ) {
        return new User(id, email, passwordHash, role, status, createdAt, updatedAt);
    }
    
    public UserId getId() { return id; }
    
    public Email getEmail() { return email; }
    
    // it was decided to treat the email as final
//    public void changeEmail(Email newEmail) {
//        Objects.requireNonNull(newEmail, "Email cannot be null");
//        if (!this.email.equals(newEmail)) {
//            this.email = newEmail;
//            touch();
//        }
//    }
    
    public String getPasswordHash() { return passwordHash; }
    
    public void updatePassword(String newPasswordHash) {
    	String validatedHash = validatePasswordHash(newPasswordHash);
        if (!this.passwordHash.equals(validatedHash)) {
            this.passwordHash = validatedHash;
            touch();
        }
    }
    
    public UserRole getRole() { return role; }
    
//    there should be no possibility of changing the role.
//    we should even think of a way to prevent the creation of ADMIN users
//    public void changeRole(UserRole role) {
//        this.role = Objects.requireNonNull(role, "Role required");
//        touch();
//    }
    
    public UserStatus getStatus() { return status; }
    
    public void updateStatus(UserStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
        touch();
    }
    
    public Instant getCreatedAt() { return createdAt; }
    
    public Instant getUpdatedAt() { return updatedAt; }
    
    // Query methods
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }
    
    public boolean isBlocked() {
        return this.status == UserStatus.BLOCKED;
    }
    
    public boolean isInactive() {
        return this.status == UserStatus.INACTIVE;
    }
    
    public boolean isCandidate() {
        return this.role == UserRole.CANDIDATE;
    }
    
    public boolean isConsultant() {
        return this.role == UserRole.CONSULTANT;
    }
    
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
    
    public boolean canLogin() {
        return isActive();
    }
    
    private void touch() { this.updatedAt = Instant.now(); }
    
    private static String validatePasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be null or empty");
        }
        String trimmed = passwordHash.trim();
        if (trimmed.length() < 8) {
            throw new IllegalArgumentException("Password hash too short (minimum 8 characters)");
        }
        return trimmed;
    }
    
    @Override
    public boolean equals(Object o) {
        return this == o
            || (o instanceof User other && id.equals(other.id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
    	return "User{" +
                "id=" + id +
                ", email=" + email +
                ", role=" + role +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

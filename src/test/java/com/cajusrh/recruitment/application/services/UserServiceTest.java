package com.cajusrh.recruitment.application.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.cajusrh.recruitment.application.service.UserService;
import com.cajusrh.recruitment.core.domain.entities.User;
import com.cajusrh.recruitment.core.domain.enums.UserRole;
import com.cajusrh.recruitment.core.domain.enums.UserStatus;
import com.cajusrh.recruitment.core.domain.valueobjects.Email;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;
import com.cajusrh.recruitment.core.ports.out.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test") 
public class UserServiceTest {
	@Mock 
	private UserRepositoryPort userRepository;
    @Mock 
    private PasswordEncoder passwordEncoder;
    @InjectMocks 
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepositoryPort.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void createUser_success() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode("passMaluco")).thenReturn("hashedMaluco");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.createUser(new Email("test@example.com"), "passMaluco", UserRole.CANDIDATE);
        assertNotNull(user.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_duplicateEmail() {
        when(userRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(new Email("x@x.com"), "pw", UserRole.CANDIDATE));
    }

    @Test
    void updateStatus_success() {
        User u = User.create(new Email("a@b.com"), "hashMaluco", UserRole.CANDIDATE);
        when(userRepository.findById(u.getId())).thenReturn(Optional.of(u));
        when(userRepository.save(u)).thenReturn(u);

        User updated = userService.updateUserStatus(u.getId(), UserStatus.BLOCKED);
        assertEquals(UserStatus.BLOCKED, updated.getStatus());
    }

    @Test
    void updatePassword_success() {
        User u = User.create(new Email("a@b.com"), "oldhashMaluco", UserRole.CANDIDATE);
        when(userRepository.findById(u.getId())).thenReturn(Optional.of(u));
        when(passwordEncoder.encode("newpwMaluco")).thenReturn("newhashMaluco");
        when(userRepository.save(u)).thenReturn(u);

        User updated = userService.updateUserPassword(u.getId(), "newpwMaluco");
        assertEquals("newhashMaluco", updated.getPasswordHash());
    }

    @Test
    void deleteUser_success() {
        UserId id = UserId.generate();
//        when(userRepository.findById(id)).thenReturn(Optional.of(mock(User.class)));
        doNothing().when(userRepository).deleteById(id);

        assertDoesNotThrow(() -> userService.deleteUser(id));
        verify(userRepository).deleteById(id);
    }
}

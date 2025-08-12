package com.cajusrh.recruitment.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.cajusrh.recruitment.api.dto.UserDto;
import com.cajusrh.recruitment.api.mappers.UserDtoMapper;
import com.cajusrh.recruitment.application.service.UserService;
import com.cajusrh.recruitment.core.domain.entities.User;
import com.cajusrh.recruitment.core.domain.enums.UserRole;
import com.cajusrh.recruitment.core.domain.enums.UserStatus;
import com.cajusrh.recruitment.core.domain.valueobjects.Email;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserControllerTest.MockedBeansConfig.class)
@ActiveProfiles("test") 
public class UserControllerTest {
	@Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @TestConfiguration
    static class MockedBeansConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        public UserDtoMapper userDtoMapper() {
        	return Mockito.mock(UserDtoMapper.class);
        }
    }

    @Test
    @WithMockUser
    void shouldReturnUserDtoWhenUserExists() throws Exception {
    	UUID id = UUID.randomUUID();
        Email testEmail = new Email("email@example.com");
        User fakeUser = User.load(
        		UserId.of(id),
                testEmail,
                "password",
                UserRole.CANDIDATE,
                UserStatus.ACTIVE,
                Instant.now(),
                Instant.now()
        );
            
        UserDto expectedDto = new UserDto(
            id, 
            testEmail.getValue(), 
            fakeUser.getRole(), 
            fakeUser.getStatus(),
            fakeUser.getCreatedAt(), 
            fakeUser.getUpdatedAt()
        );

        Mockito.when(userService.findById(eq(UserId.of(id))))
            .thenReturn(Optional.of(fakeUser));
            
        Mockito.when(userDtoMapper.toDto(any(User.class)))
            .thenReturn(expectedDto);

        mvc.perform(get("/users/" + id)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.email").value(testEmail.getValue()));
    }
}

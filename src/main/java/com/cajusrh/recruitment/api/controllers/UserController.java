package com.cajusrh.recruitment.api.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cajusrh.recruitment.api.dto.UserDto;
import com.cajusrh.recruitment.api.mappers.UserDtoMapper;
import com.cajusrh.recruitment.application.service.UserService;
import com.cajusrh.recruitment.core.domain.entities.User;
import com.cajusrh.recruitment.core.domain.valueobjects.UserId;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	private final UserDtoMapper userDtoMapper;

    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }
    
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateUserRequest req) {
        User id = userService.createUser(
    		req.email(), 
    		req.password(), 
    		req.role()
        );
        return ResponseEntity.created(URI.create("/users/" + id.getId())).build();
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") UUID id,
    											@RequestBody UpdateStatusRequest req) {
        userService.updateUserStatus(UserId.of(id), req.status());
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") UUID id) {
        return userService.findById(UserId.of(id))
            .map(userDtoMapper::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}

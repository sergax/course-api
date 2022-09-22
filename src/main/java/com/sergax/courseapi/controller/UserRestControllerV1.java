package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRestControllerV1 {
    private final UserService userService;

    @GetMapping("/all")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping
    @Secured(value = "ROLE_USER, ROLE_ADMIN")
    public ResponseEntity<UserDto> findUserByAuth(Principal principal) {
        return ResponseEntity.ok(userService.findUserByEmail(principal.getName()));
    }

    @GetMapping("/{userId}")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PostMapping
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.save(userDto), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(userId, userDto));
    }

    @PatchMapping("/{userId}/{roleId}")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<UserDto> addRoleToUser(@PathVariable Long userId,
                                                 @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.addRoleForUserById(userId, roleId));
    }

    @DeleteMapping("/{userId}")
    @Secured(value = "ROLE_ADMIN")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

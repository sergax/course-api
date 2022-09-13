package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRestControllerV1 {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        var usersDto = userService.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long userId) {
        var userById = userService.findById(userId);
        return ResponseEntity.ok(new UserDto(userById));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        var savedUser = userService.save(userDto.toUser());
        return new ResponseEntity<>(new UserDto(savedUser), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @RequestBody UserDto userDto) {
        var updatedUser = userService.update(userId, userDto.toUser());
        return ResponseEntity.ok(new UserDto(updatedUser));
    }

    @PatchMapping("/{userId}/{roleId}")
    public ResponseEntity<UserDto> addRoleToUser(@PathVariable Long userId,
                                                 @PathVariable Long roleId) {
        return ResponseEntity.ok(userService.addRoleForUserById(userId, roleId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

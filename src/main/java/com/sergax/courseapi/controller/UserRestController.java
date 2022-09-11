package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.create(userDto), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(userId, userDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

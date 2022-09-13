package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.*;
import com.sergax.courseapi.model.ConfirmationCode;
import com.sergax.courseapi.model.Role;
import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.User;
import com.sergax.courseapi.security.JwtTokenProvider;
import com.sergax.courseapi.service.iml.UserServiceIml;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final AuthenticationManager authenticationManager;
    private final UserServiceIml userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));

        User user = userService.toUser(userService.findUserByEmail(email));

        if (user.getStatus().equals(Status.NOT_ACTIVE)) {
            throw new EntityNotFoundException
                    ("This is your first login. You must change your previously generated password");
        }

        String token = jwtTokenProvider.createToken(user);
        List<String> roles = user.getRoles()
                .stream().map(Role::getName)
                .collect(Collectors.toList());

        String role = roles.contains("ROLE_ADMIN") ? "ADMIN" : "USER";

        LoginResponseDto responseDto = new LoginResponseDto(user.getId(), email, token, role);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody UserDto userDto) {
        return new  ResponseEntity<>(userService.register(userDto), HttpStatus.ACCEPTED);
    }

    @PostMapping("/confirmation")
    public ResponseEntity<UserDto> emailConfirmation(@RequestBody @Valid ConfirmationCodeDto confirmationCodeDto) {
        ConfirmationCode confirmationCode = confirmationCodeDto.toConfirmationCode();

        User user = userService.toUser(userService.confirmEmail(confirmationCode));
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.GONE);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}

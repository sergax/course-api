package com.sergax.courseapi.controller;

import com.sergax.courseapi.dto.*;
import com.sergax.courseapi.model.user.ConfirmationCode;
import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.user.Role;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.security.JwtTokenProvider;
import com.sergax.courseapi.service.CourseService;
import com.sergax.courseapi.service.UserService;
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

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestControllerV1 {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CourseService courseService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Validated LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));

        User user = userService.findUserByEmail(email).toUser();

        if (user.getStatus().equals(Status.NOT_ACTIVE)) {
            throw new EntityNotFoundException
                    ("This is your first login. You must change your previously generated password");
        }

        String token = jwtTokenProvider.createToken(user);
        List<String> roles = user.getRoles()
                .stream().map(Role::getName).toList();

        String role = roles.contains("ROLE_ADMIN") ? "ADMIN" : "USER";

        LoginResponseDto responseDto = new LoginResponseDto(user.getId(), email, token, role);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody @Valid RegistrationDto registrationDto) {
        var userDto = registrationDto.toUserDto();
        return new ResponseEntity<>(userService.register(userDto), HttpStatus.ACCEPTED);
    }

    @PostMapping("/confirmation")
    public ResponseEntity<UserDto> emailConfirmation(@RequestBody @Valid ConfirmationCodeDto confirmationCodeDto) {
        ConfirmationCode confirmationCode = confirmationCodeDto.toConfirmationCode();

        User user = userService.confirmEmail(confirmationCode).toUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.GONE);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}

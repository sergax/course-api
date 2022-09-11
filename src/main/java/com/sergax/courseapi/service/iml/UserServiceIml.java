package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.ConfirmationCode;
import com.sergax.courseapi.model.User;
import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.repository.RoleRepository;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.security.JwtTokenProvider;
import com.sergax.courseapi.service.exception.NotUniqueDataException;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random;
    private final JwtTokenProvider jwtTokenProvider;

    private static final long MILLISECONDS_IN_HOUR = 3_600_000;
    private static final Integer PASSWORD_LENGTH = 8;
    private static final Integer NUMBER_SPECIAL_CHARS_IN_PASSWORD = 2;
    private static final String AVAILABLE_CHARS_FOR_PASSWORD = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final String REQUIRED_SYMBOLS_FOR_PASSWORD = "^$?!@#%&";
    @Value("${cors.allowed.origins}")
    private String baseUrl;
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::new)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User by ID: %d not found", id)));
    }


    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        if (existsUserByEmail(userDto.getEmail())) {
            throw new NotUniqueDataException(
                    format("User by email: %s , already exists", userDto.getEmail()));
        }
        userDto.setCreated(LocalDate.now());
        userDto.setUpdated(LocalDate.now());
        userDto.setStatus(Status.ACTIVE);
        var user = toUser(userDto);
        userRepository.save(user);

        log.info("IN create user: {}", new UserDto(user));
        return new UserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UserDto userDto) {
        UserDto userById = findById(userId);
        if (existsUserByEmail(userDto.getEmail())) {
            throw new NotUniqueDataException(
                    format("User by email: %s , already exists", userDto.getEmail()));
        }
        userById
                .setEmail(userDto.getEmail())
                .setUpdated(LocalDate.now())
                .setPassword(userDto.getPassword())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setRolesId(userDto.getRolesId());

        userRepository.save(toUser(userById));
        log.info("In update user: {}", userById);
        return userById;
    }

    @Override
    public void deleteById(Long id) {
        UserDto userById = findById(id);
        userById.setStatus(Status.DELETED);
        log.info("In deleteById user was deleted: {}", userById);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserDto::new)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User by email: %s, not found", email)));
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto addRoleForUserById(Long userId, Long roleId) {
        var userByEmail = findById(userId);
        var roleById = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Role by ID: %d, not found", roleId)));
        userByEmail.getRolesId().add(roleById.getId());
        roleById.getUsers().add(toUser(userByEmail));
        userRepository.save(toUser(userByEmail));
        roleRepository.save(roleById);

        log.info("IN addRoleForUserByEmail: {}", userId);
        return userByEmail;
    }

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {
        existsUserByEmail(userDto.getEmail());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userDto.setRolesId(new HashSet<>());
        userDto.getRolesId().add(roleRepository.findById(2L);

        User registeredUser = userRepository.save(toUser(userDto));

        int code = random.nextInt(900_000) + 100_000;
        Date expirationDate = new Date(new Date().getTime() + MILLISECONDS_IN_HOUR);
        ConfirmationCode confirmationCode = new ConfirmationCode(String.valueOf(code), userDto.getEmail(), expirationDate, Status.ACTIVE);
        confirmationCodeRepository.save(confirmationCode);

        log.info("IN register user {} registered", registeredUser);
        return new UserDto(registeredUser);
    }

    public User toUser(UserDto userDto) {
        var user = new User()
                .setId(userDto.getId())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setPassword(userDto.getPassword())
                .setEmail(userDto.getEmail())
                .setCreated(userDto.getCreated())
                .setUpdated(userDto.getUpdated())
                .setStatus(userDto.getStatus());
        if (userDto.getRolesId() == null) {
            user.setRoles(new HashSet<>());
        } else {
            user.setRoles(userDto.getRolesId().stream()
                    .map(roleRepository::findById)
                    .map(Optional::get)
                    .collect(Collectors.toSet()));
        }
        return user;
    }
}

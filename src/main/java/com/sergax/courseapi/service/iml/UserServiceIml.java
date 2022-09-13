package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.user.ConfirmationCode;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.repository.ConfirmationCodeRepository;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.service.RoleService;
import com.sergax.courseapi.service.exception.AlreadyConfirmedException;
import com.sergax.courseapi.service.exception.CodeNotFoundException;
import com.sergax.courseapi.service.exception.InvalidConfirmationCodeException;
import com.sergax.courseapi.service.exception.NotUniqueDataException;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;

    private static final Integer PASSWORD_LENGTH = 8;
    private static final Integer NUMBER_SPECIAL_CHARS_IN_PASSWORD = 2;
    private static final String AVAILABLE_CHARS_FOR_PASSWORD = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789";
    private static final String REQUIRED_SYMBOLS_FOR_PASSWORD = "^$?!@#%&";
//    @Value("${cors.allowed.origins}")
//    private String baseUrl;

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
    public UserDto save(UserDto userDto) {
        if (existsUserByEmail(userDto.getEmail())) {
            throw new NotUniqueDataException(
                    format("User by email: %s , already exists", userDto.getEmail()));
        }
        userDto.setCreated(LocalDate.now());
        userDto.setUpdated(LocalDate.now());
        userDto.setStatus(Status.ACTIVE);
        var user = toUser(userDto);
        userRepository.save(user);

        log.info("IN create user: {}", userDto);
        return userDto;
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
                .setRoles(userDto.getRoles());

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
    @Transactional
    public UserDto addRoleForUserById(Long userId, Long roleId) {
        var userDto = findById(userId);
        var roleById = roleService.findById(roleId);

        userDto.getRoles().add(new RoleDto(roleById));
        var user = toUser(userDto);
        roleById.getUsers().add(user);
        userRepository.save(user);
        roleService.save(roleById);

        log.info("IN addRoleForUserByEmail: {}", new UserDto(user));
        return new UserDto(user);
    }

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {
        if (existsUserByEmail(userDto.getEmail())) {
            throw new NotUniqueDataException(
                    format("User by email: %s, already exists", userDto.getEmail()));
        }
        var role = roleService.findById(2L);
        userDto.setCreated(LocalDate.now());
        userDto.setUpdated(userDto.getCreated());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setStatus(Status.NOT_CONFIRMED);
        userDto.setRoles(List.of(new RoleDto(role)));

        var registeredUser = userRepository.save(toUser(userDto));
        var random = new Random();
        var code = random.nextInt(900_000) + 100_000;
        var expirationDate = LocalDate.now().plus(1L, ChronoUnit.DAYS);
        var confirmationCode = new ConfirmationCode(
                String.valueOf(code),
                userDto.getEmail(),
                expirationDate,
                Status.ACTIVE);
        confirmationCodeRepository.save(confirmationCode);

        log.info("IN register user: {} registered", new UserDto(registeredUser));
        return new UserDto(registeredUser);
    }

    @Override
    public UserDto confirmEmail(ConfirmationCode confirmationCode) {
        if (!confirmationCodeRepository.existsByEmailAndStatus(confirmationCode.getEmail(), Status.ACTIVE)) {
            throw new CodeNotFoundException(
                    String.format("Active code for email %s is not found", confirmationCode.getEmail()));
        }

        var user = userRepository.findByEmail(confirmationCode.getEmail()).orElseThrow(IllegalStateException::new);

        if (user.getStatus() != Status.NOT_CONFIRMED) {
            throw new AlreadyConfirmedException(
                    String.format("User with email %s has already confirmed his email", confirmationCode.getEmail()));
        }

        var correctConfirmationCode =
                confirmationCodeRepository.getConfirmationCodeByEmailAndStatus(
                        confirmationCode.getEmail(), Status.ACTIVE);

        if (!correctConfirmationCode.getCode().equals(confirmationCode.getCode())) {
            throw new InvalidConfirmationCodeException("Invalid code");
        }

        if (correctConfirmationCode.getExpirationDate().isBefore(LocalDate.now())) {
            var random = new Random();
            var code = random.nextInt(900_000) + 100_000;
            var expirationDate = LocalDate.now().plus(1L, ChronoUnit.DAYS);
            var newConfirmationCode = new ConfirmationCode(String.valueOf(code), user.getEmail(), expirationDate, Status.ACTIVE);
            confirmationCodeRepository.saveAndFlush(newConfirmationCode);
            log.info("IN confirmEmail code {} has been created", newConfirmationCode);

            correctConfirmationCode.setStatus(Status.DELETED);
            confirmationCodeRepository.saveAndFlush(correctConfirmationCode);
            log.info("IN confirmEmail code {} has been updated", correctConfirmationCode);

            confirmationCodeRepository.flush();

//            String message = "Hello, your previous confirmation code has been expired. There is your new email confirmation code: " + newConfirmationCode.getCode();
//            mailService.send(user.getEmail(), "Email confirmation code", message);

        }

        user.setStatus(Status.ACTIVE);
        var updatesUser = userRepository.save(user);
        correctConfirmationCode.setStatus(Status.DELETED);
        confirmationCodeRepository.save(correctConfirmationCode);

        return new UserDto(updatesUser);
    }

    public User toUser(UserDto userDto) {
        var user = new User()
                .set(userDto.getId())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setPassword(userDto.getPassword())
                .setEmail(userDto.getEmail())
                .setCreated(userDto.getCreated())
                .setUpdated(userDto.getUpdated())
                .setStatus(userDto.getStatus());
        if (userDto.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        } else {
            user.setRoles(userDto.getRoles().stream()
                    .map(roleDto -> roleService.findById(roleDto.getId()))
                    .collect(Collectors.toList()));
        }
        return user;
    }
}

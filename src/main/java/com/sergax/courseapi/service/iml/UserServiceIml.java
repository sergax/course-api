package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.user.ConfirmationCode;
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
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
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
        var user = userDto.toUser();
        userRepository.save(user);
        return new UserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UserDto userDto) {
        var userById = userRepository.getById(userId);
        userById
                .setEmail(userDto.getEmail())
                .setUpdated(LocalDate.now())
                .setPassword(userById.getPassword())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName());

        log.info("In update user: {}", new UserDto(userById));
        return new UserDto(userById);
    }

    @Override
    public void deleteById(Long id) {
        var userById = findById(id);
        userById.setStatus(Status.DELETED);
        log.info("In deleteById user was deleted: {}", userById);
    }

    @Override
    @Transactional
    public UserDto findUserByEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("User by email: %s, not found", email)));
        Hibernate.initialize(user.getRoles());
        return new UserDto(user);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserDto addRoleForUserById(Long userId, Long roleId) {
        var user = findById(userId);
        var role = roleService.findById(roleId);
        user.getRoles().add(role);

        log.info("IN addRoleForUserByEmail: {}", user);
        return user;
    }

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {
        if (existsUserByEmail(userDto.getEmail())) {
            throw new NotUniqueDataException(
                    format("User by email: %s, already exists", userDto.getEmail()));
        }
        var role = roleService.findById(2L);
        var roles = new ArrayList<RoleDto>();
        userDto.setCreated(LocalDate.now());
        userDto.setUpdated(userDto.getCreated());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setStatus(Status.NOT_CONFIRMED);
        roles.add(role);
        userDto.setRoles(roles);

        var registeredUser = save(userDto);
        var random = new Random();
        var code = random.nextInt(900_000) + 100_000;
        var expirationDate = LocalDate.now().plus(1L, ChronoUnit.DAYS);
        var confirmationCode = new ConfirmationCode(
                String.valueOf(code),
                userDto.getEmail(),
                expirationDate,
                Status.ACTIVE);
        confirmationCodeRepository.save(confirmationCode);

        log.info("IN register user: {} registered", registeredUser);
        return registeredUser;
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

}

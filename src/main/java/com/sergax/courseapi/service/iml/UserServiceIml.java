package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.UserStatus;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.service.exception.NotUniqueDataException;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {
    private final UserRepository userRepository;

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

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        if (existsUserByEmail(userDto.getEmail())) {
            throw new NotUniqueDataException(
                    format("User by email: %s , already exists", userDto.getEmail()));
        }
        userDto.setCreated(LocalDate.now());
        userDto.setStatus(UserStatus.ACTIVE);
        var user = userDto.toUser();
        userRepository.save(userDto.toUser());
        log.info("IN create user: {}", userDto);
        return new UserDto(user);
    }

    @Transactional
    @Override
    public UserDto update(Long userId, UserDto userDto) {
        UserDto userById = findById(userId);
        if (existsUserByEmail(userDto.getEmail())) {
            throw new NotUniqueDataException(
                    format("User by email: %s , already exists", userDto.getEmail()));
        }
        userById.setEmail(userDto.getEmail())
                .setUpdated(LocalDate.now())
                .setPassword(userDto.getPassword())
                .setFirstName(userDto.getFirstName())
                .setLastName(userDto.getLastName())
                .setRoleDto(userDto.getRoleDto());

        log.info("In update user: {}", userById);
        return userById;
    }

    @Override
    public void deleteById(Long id) {
        UserDto userById = findById(id);
        userById.setStatus(UserStatus.DELETED);
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
}

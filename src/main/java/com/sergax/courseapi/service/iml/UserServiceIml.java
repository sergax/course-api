package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.User;
import com.sergax.courseapi.model.UserStatus;
import com.sergax.courseapi.repository.RoleRepository;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.service.exception.NotUniqueDataException;
import com.sergax.courseapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceIml implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

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
        userDto.setStatus(UserStatus.ACTIVE);
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

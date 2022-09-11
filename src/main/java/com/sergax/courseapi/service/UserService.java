package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.User;

import java.util.Optional;

public interface UserService extends BaseService<UserDto, Long> {
    UserDto findUserByEmail(String email);
    boolean existsUserByEmail(String email);
}

package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.UserDto;

public interface UserService extends BaseService<UserDto, Long> {
    UserDto findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    UserDto addRoleForUserById(Long userId, Long roleId);
}

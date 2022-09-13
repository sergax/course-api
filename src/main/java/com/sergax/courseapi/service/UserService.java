package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.user.ConfirmationCode;
import com.sergax.courseapi.model.user.User;

public interface UserService extends BaseService<User, Long> {
    UserDto findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    UserDto addRoleForUserById(Long userId, Long roleId);
    UserDto register(User user);
    UserDto confirmEmail(ConfirmationCode confirmationCode);
}

package com.sergax.courseapi.service;

import com.sergax.courseapi.dto.ConfirmationCodeDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.user.ConfirmationCode;
import com.sergax.courseapi.model.user.User;

public interface UserService extends BaseService<UserDto, Long> {
    UserDto findUserByEmail(String email);
    boolean existsUserByEmail(String email);
    UserDto addRoleForUserById(Long userId, Long roleId);
    UserDto register(UserDto userDto);
    UserDto confirmEmail(ConfirmationCode confirmationCode);
}

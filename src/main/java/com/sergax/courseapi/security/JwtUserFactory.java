package com.sergax.courseapi.security;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUserFactory {

    public static JwtUser createJwtUser(UserDto user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Status.ACTIVE.equals(user.getStatus()),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static Set<GrantedAuthority> mapToGrantedAuthorities(Set<RoleDto> roles) {
        return roles.stream()
                .map(RoleDto::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}

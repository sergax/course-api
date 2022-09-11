package com.sergax.courseapi.security;

import com.sergax.courseapi.model.Role;
import com.sergax.courseapi.model.User;
import com.sergax.courseapi.model.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUserFactory {

    public static JwtUser createJwtUser(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Status.ACTIVE.equals(user.getStatus()),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static Set<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}

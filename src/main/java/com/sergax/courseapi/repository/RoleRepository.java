package com.sergax.courseapi.repository;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

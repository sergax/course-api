package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.model.user.Role;
import com.sergax.courseapi.repository.RoleRepository;
import com.sergax.courseapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceIml implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        log.info("In findById role ID: {}", id);
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Role by ID: %d, not found", id)));
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role update(Long id, Role role) {
        var existingRole = roleRepository.findById(id).orElseThrow();
        existingRole.setName(role.getName());
        return existingRole;
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}

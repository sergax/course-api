package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.repository.RoleRepository;
import com.sergax.courseapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceIml implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<RoleDto> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDto findById(Long id) {
        log.info("In findById role ID: {}", id);
        return roleRepository.findById(id)
                .map(RoleDto::new)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Role by ID: %d, not found", id)));
    }

    @Override
    public RoleDto save(RoleDto roleDto) {
        var role = roleDto.toRole();
        var savedRole = roleRepository.save(role);
        return new RoleDto(savedRole);
    }

    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        var existingRole = roleRepository.findById(id).orElseThrow();
        existingRole.setName(roleDto.getName());
        return new RoleDto(existingRole);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}

package com.sergax.courseapi.service.iml;

import com.sergax.courseapi.dto.RoleDto;
import com.sergax.courseapi.dto.UserDto;
import com.sergax.courseapi.model.Status;
import com.sergax.courseapi.model.user.ConfirmationCode;
import com.sergax.courseapi.model.user.Role;
import com.sergax.courseapi.model.user.User;
import com.sergax.courseapi.repository.ConfirmationCodeRepository;
import com.sergax.courseapi.repository.UserRepository;
import com.sergax.courseapi.service.MailService;
import com.sergax.courseapi.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImlTest {
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private RoleService roleServiceMock;
    @Mock
    private ConfirmationCodeRepository confirmationCodeRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private MailService mailServiceMock;
    @InjectMocks
    private UserServiceIml userServiceImlTest;
    private final User userTest = new User();
    private final Role roleTest = new Role();

    @BeforeEach
    void setUp() {
        roleTest
                .setId(2L)
                .setName("ROLE_USER");
        userTest
                .setId(500L)
                .setRoles(List.of(new Role()))
                .setEmail("email@com")
                .setFirstName("name")
                .setStatus(Status.NOT_CONFIRMED);
    }

    @Test
    void findAll() {
        userServiceImlTest.findAll();
        verify(userRepositoryMock).findAll();
    }

    @Test
    void findById() {
        when(userRepositoryMock.findById(userTest.getId())).thenReturn(Optional.of(userTest));
        userServiceImlTest.findById(userTest.getId());
        verify(userRepositoryMock).findById(userTest.getId());
    }

    @Test
    void save() {
        var expectedUser = new UserDto(userTest);
        when(userRepositoryMock.save(userTest)).thenReturn(userTest);
        var actualUser = userServiceImlTest.save(expectedUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void update() {
        var expectedUser = new UserDto(userTest);
        expectedUser
                .setFirstName("new name")
                .setEmail("newMentor@mail.com");
        when(userRepositoryMock.getById(userTest.getId())).thenReturn(userTest);
        var actualUser = userServiceImlTest.update(expectedUser.getId(), expectedUser);
        assertEquals("new name", actualUser.getFirstName());
        assertEquals("newMentor@mail.com", actualUser.getEmail());
    }

    @Test
    void deleteById() {
        var expectedUser = new UserDto(userTest);
        expectedUser
                .setStatus(Status.DELETED);
        when(userRepositoryMock.findById(userTest.getId())).thenReturn(Optional.of(userTest));
        userServiceImlTest.deleteById(expectedUser.getId());
        assertEquals(Status.DELETED, expectedUser.getStatus());
    }

    @Test
    void findUserByEmail() {
        when(userRepositoryMock.findByEmail(userTest.getEmail())).thenReturn(Optional.of(userTest));
        userServiceImlTest.findUserByEmail(userTest.getEmail());
        verify(userRepositoryMock).findByEmail(userTest.getEmail());
    }

    @Test
    void shouldThrow_canNotFindUserByEmail() {
        when(userRepositoryMock.findByEmail(userTest.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImlTest.findUserByEmail(userTest.getEmail()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User by email: %s, not found", userTest.getEmail());
    }

    @Test
    void existsUserByEmail() {
        when(userRepositoryMock.existsByEmail(userTest.getEmail())).thenReturn(true);
        Assertions.assertTrue(userServiceImlTest.existsUserByEmail(userTest.getEmail()));
    }

    @Test
    void addRoleForUserById() {
        var roleDto = new RoleDto(roleTest);
        when(userRepositoryMock.findById(userTest.getId())).thenReturn(Optional.of(userTest));
        when(roleServiceMock.findById(roleTest.getId())).thenReturn(roleDto);
        var actualUser =
                userServiceImlTest.addRoleForUserById(userTest.getId(), roleTest.getId());
        assertEquals(2, actualUser.getRoles().size());
    }

    @Test
    void register() {
        var userDto = new UserDto();
        var roleDto = new RoleDto(roleTest);
        when(userRepositoryMock.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(roleServiceMock.findById(roleTest.getId())).thenReturn(roleDto);

        var actualUser =
                userServiceImlTest.register(userDto);
        assertEquals(1, actualUser.getRoles().size());
        assertEquals(Status.NOT_CONFIRMED, actualUser.getStatus());
        assertEquals(LocalDate.now(), actualUser.getCreated());
        assertEquals(LocalDate.now(), actualUser.getCreated());
    }

    @Test
    void confirmEmail() {
        var random = new Random();
        var expectedUser = new UserDto(userTest);
        expectedUser.setStatus(Status.ACTIVE);
        var confirmationCode = new ConfirmationCode();
        confirmationCode.setCode(String.valueOf(random.nextInt(900_00) + 100_000));
        confirmationCode.setExpirationDate(LocalDate.now().plus(1L, ChronoUnit.DAYS));
        confirmationCode.setStatus(Status.DELETED);
        confirmationCode.setEmail(userTest.getEmail());
        when(confirmationCodeRepositoryMock.existsByEmailAndStatus(
                confirmationCode.getEmail(), Status.ACTIVE))
                .thenReturn(true);
        when(userRepositoryMock.findByEmail(confirmationCode.getEmail()))
                .thenReturn(Optional.of(userTest));
        when(confirmationCodeRepositoryMock.getConfirmationCodeByEmailAndStatus(
                confirmationCode.getEmail(), Status.ACTIVE))
                .thenReturn(confirmationCode);
        when(userRepositoryMock.save(userTest)).thenReturn(userTest);

        var actualUser = userServiceImlTest.confirmEmail(confirmationCode);
        assertEquals(expectedUser, actualUser);
    }
}
package com.paca.paca.user;

import com.paca.paca.user.dto.UserRequestDTO;
import com.paca.paca.user.dto.UserResponseDTO;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.utils.UserMapper;
import com.paca.paca.user.service.UserService;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.exception.exceptions.NoContentException;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import com.paca.paca.utils.TestUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import junit.framework.TestCase;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetAllUsers() {
        List<User> user = TestUtils.castList(User.class, Mockito.mock(List.class));

        when(userRepository.findAll()).thenReturn(user);
        UserListDTO responseDTO = userService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetUserById() {
        Long id = 1L;
        Role role = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        User user = User.builder()
                .id(id).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role(role)
                .build();

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(1L).email("test@test.com").verified(false).loggedIn(false).role(role.getName().name())
                .build();

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userResponseDTO);
        UserResponseDTO responseDTO = userService.getById(user.getId());

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(id);
    }

    @Test
    void shouldGetBadRequestFromGetUserById() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        try {
            userService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assertions.assertTrue(e instanceof NoContentException);
            Assertions.assertEquals(e.getMessage(), "User with id 1 does not exists");
        }
    }

    @Test
    void shouldUpdateUserById() {
        String passHash = "$2a$10$3JJoqbyYXtUDCWt9.H7wKOXeBsEAv3R5uf30qA/8QtCu9GFjqjWSa";
        Role admin = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        Role client = Role.builder().id((long) UserRole.client.ordinal()).name(UserRole.client).build();
        User user = User.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role(admin)
                .build();
        User expected = User.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(true).loggedIn(false).role(client)
                .build();
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .id(1L).email("test2@test.com").password("123456789aB$").verified(true).loggedIn(true)
                .role(UserRole.client.name())
                .build();
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(1L).email("test@test.com").verified(true).loggedIn(false).role(UserRole.client.name())
                .build();

        when(passwordEncoder.encode(any(String.class))).thenReturn(passHash);
        when(userMapper.toDTO(any(User.class))).thenReturn(userResponseDTO);
        when(userMapper.updateEntity(any(UserRequestDTO.class), any(User.class), any(UserRole.class))).thenReturn(expected);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserResponseDTO response = userService.update(1L, userRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getVerified()).isEqualTo(userRequestDTO.getVerified());
    }

    @Test
    void shouldGetBadRequestFromUpdateUserByIdBecauseUserDoesNotExists() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        try {
            userService.update(1L, UserRequestDTO.builder().build());
        } catch (NoContentException e){
            assertThat(e.getMessage()).isEqualTo("User with id 1 does not exists");
            assertThat(e.getCode()).isEqualTo(12);
        }
    }
}
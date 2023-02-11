package com.paca.paca.user;

import com.paca.paca.PacaTest;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.user.service.UserService;
import com.paca.paca.user.utils.UserMapper;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import junit.framework.TestCase;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetAllUsers() {
        List<User> user = Mockito.mock(List.class);

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

        UserDTO userDTO = UserDTO.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false)
                .role(role.getName().name())
                .build();

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        UserDTO responseDTO = userService.getById(id);

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
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "User does not exists");
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
                .id(1L).email("test@test.com").password("123456789aB#").verified(false).loggedIn(false).role(client)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .id(1L).email("test2@test.com").password("123456789aB$").verified(true).loggedIn(true)
                .role(UserRole.client.name())
                .build();

        when(passwordEncoder.encode(any(String.class))).thenReturn(passHash);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        when(userMapper.updateEntity(any(UserDTO.class), any(User.class), any(UserRole.class))).thenReturn(expected);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDTO response = userService.update(1L, userDTO);

        assertThat(response).isNotNull();
        assertThat(response.getPassword()).isEqualTo(userDTO.getPassword());
    }

    @Test
    void shouldGetBadRequestFromUpdateUserByIdBecauseUserDoesNotExists() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        try {
            userService.update(1L, UserDTO.builder().build());
        } catch (Exception e){
            System.out.println(e.getMessage());
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "User does not exists");
        }
    }

    @Test
    @Disabled
    void shouldGetBadRequestFromUpdateUserByIdBecauseEmailIsTaken() {
        Role admin = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        User user = User.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role(admin)
                .build();

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        //when(userRepository.existsByEmail(any(String.class))).thenReturn(false);
        try {
            userService.update(1L, UserDTO.builder().build());
        } catch (Exception e){
            System.out.println(e.getMessage());

            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals(e.getMessage(), "This email is already taken");
        }
    }
}
package com.paca.paca.user;

import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.utils.UserMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    @Mock
    private UserMapper userMapper;

    @Test
    void shouldMapUserEntityToUserDTO() {
        Role role = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        User user = User.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role(role)
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false)
                .role(role.getName().name())
                .build();

        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        UserDTO response = userMapper.toDTO(user);

        assertThat(response).isNotNull();
        assertThat(response.getRole()).isEqualTo(role.getName().name());
    }

    @Test
    void shouldMapUserDTOtoUserEntity() {
        Role role = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        User user = User.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role(role)
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(1L).email("test@test.com").password("123456789aA#").role(role.getName().name())
                .build();

        when(userMapper.toEntity(any(UserDTO.class), any(UserRole.class))).thenReturn(user);
        User response = userMapper.toEntity(userDTO, role.getName());

        assertThat(response).isNotNull();
        assertThat(response.getRole()).isEqualTo(role);
        assertThat(response.getVerified()).isFalse();
        assertThat(response.getLoggedIn()).isFalse();
    }

    @Test
    void shouldPartiallyMapUserDTOtoUserEntity() {
        Role admin = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.admin).build();
        Role client = Role.builder().id((long) UserRole.admin.ordinal()).name(UserRole.client).build();
        User user = User.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role(admin)
                .build();

        User expected = User.builder()
                .id(1L).email("test@test.com").password("123456789aB#").verified(false).loggedIn(false).role(client)
                .build();

        UserDTO userDTO = UserDTO.builder()
                .email("").password("123456789aB#").verified(true).loggedIn(true).role(client.getName().name())
                .build();

        when(userMapper.updateEntity(any(UserDTO.class), any(User.class), any(UserRole.class))).thenReturn(expected);
        User response = userMapper.updateEntity(userDTO, user, UserRole.client);

        assertThat(response).isNotNull();
        assertThat(response.getRole().getName().name()).isEqualTo(userDTO.getRole());
        assertThat(response.getPassword()).isEqualTo(userDTO.getPassword());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getVerified()).isEqualTo(user.getVerified());
        assertThat(response.getLoggedIn()).isEqualTo(user.getLoggedIn());
    }

}

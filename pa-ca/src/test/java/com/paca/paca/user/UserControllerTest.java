package com.paca.paca.user;

import com.paca.paca.user.dto.UserResponseDTO;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.user.model.User;
import com.paca.paca.user.dto.UserRequestDTO;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.user.statics.UserStatics;
import com.paca.paca.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.user.controller.UserController;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetUserList() throws Exception {
        utils.setAuthorities("admin");
        UserResponseDTO userResponseDTO = utils.createUserResponseDTO(null);

        ArrayList<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        userResponseDTOList.add(userResponseDTO);

        UserListDTO userListDTO = UserListDTO.builder().users(userResponseDTOList).build();
        when(userService.getAll()).thenReturn(userListDTO);

        mockMvc.perform(get(UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_ALL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetUserById() throws Exception {
        utils.setAuthorities("admin");
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(1L).email("test@test.com").verified(false).loggedIn(false)
                .role("client")
                .build();
        when(userService.getById(anyLong())).thenReturn(userResponseDTO);

        ResultActions response = mockMvc
                .perform(get((UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_BY_ID).replace("{id}", "1"))
                        .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(userResponseDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(userResponseDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified",
                        CoreMatchers.is(userResponseDTO.getVerified())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loggedIn",
                        CoreMatchers.is(userResponseDTO.getLoggedIn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role",
                        CoreMatchers.is(userResponseDTO.getRole())));
    }

    @Test
    public void shouldThrowBadRequestExceptionInGetUserById() throws Exception {
        utils.setAuthorities("admin");
        when(userService.getById(anyLong())).thenThrow(new BadRequestException("message", 0));

        ResultActions response = mockMvc
                .perform(get((UserStatics.Endpoint.PATH + UserStatics.Endpoint.GET_BY_ID).replace("{id}", "1"))
                        .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateUserById() throws Exception {
        User user = utils.createUser();
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .password(user.getPassword())
                .role(user.getRole().getName().name())
                .build();
        UserResponseDTO userResponseDTO = utils.createUserResponseDTO(user);
        utils.setAuthorities(user.getRole().getName().name());

        when(userService.update(anyLong(), any(UserRequestDTO.class))).thenReturn(userResponseDTO);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        ResultActions response = mockMvc.perform(patch(
                (UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}", user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(userResponseDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(userResponseDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified",
                        CoreMatchers.is(userResponseDTO.getVerified())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loggedIn",
                        CoreMatchers.is(userResponseDTO.getLoggedIn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role",
                        CoreMatchers.is(userRequestDTO.getRole())));
    }

    @Test
    public void shouldThrowBadRequestExceptionInUpdateUserById() throws Exception {
        User user = utils.createUser();
        utils.setAuthorities(user.getRole().getName().name());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(userService.update(anyLong(), any(UserRequestDTO.class)))
                .thenThrow(new BadRequestException("message", 0));

        ResultActions response = mockMvc.perform(patch(
                (UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}", user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserRequestDTO.builder().build())));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldThrowUnprocessableExceptionInUpdateUserById() throws Exception {
        User user = utils.createUser();
        utils.setAuthorities(user.getRole().getName().name());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(userService.update(anyLong(), any(UserRequestDTO.class)))
                .thenThrow(new UnprocessableException("message", 0));

        ResultActions response = mockMvc.perform(patch(
                (UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}", user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserRequestDTO.builder().build())));

        response
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldThrowConflictExceptionInUpdateUserById() throws Exception {
        User user = utils.createUser();
        utils.setAuthorities(user.getRole().getName().name());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(userService.update(anyLong(), any(UserRequestDTO.class))).thenThrow(new ConflictException("message", 0));

        ResultActions response = mockMvc.perform(patch(
                (UserStatics.Endpoint.PATH + UserStatics.Endpoint.UPDATE).replace("{id}", user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserRequestDTO.builder().build())));

        response
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteUserById() throws Exception {
        User user = utils.createUser();
        utils.setAuthorities(user.getRole().getName().name());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        doNothing().when(userService).delete(anyLong());

        ResultActions response = mockMvc.perform(delete(
                (UserStatics.Endpoint.PATH + UserStatics.Endpoint.DELETE).replace("{id}", user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldThrowBadRequestExceptionInDeleteUserById() throws Exception {
        User user = utils.createUser();
        utils.setAuthorities(user.getRole().getName().name());

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        doThrow(new BadRequestException("message", 0)).when(userService).delete(anyLong());

        ResultActions response = mockMvc.perform(delete(
                (UserStatics.Endpoint.PATH + UserStatics.Endpoint.DELETE).replace("{id}", user.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }
}
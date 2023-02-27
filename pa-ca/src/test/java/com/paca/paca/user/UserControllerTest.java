package com.paca.paca.user;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.user.model.User;
import com.paca.paca.user.dto.UserDTO;
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
        UserDTO userDTO = utils.createUserDTO(null);

        ArrayList<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);

        UserListDTO userListDTO = UserListDTO.builder().users(userDTOList).build();
        when(userService.getAll()).thenReturn(userListDTO);

        mockMvc.perform(get(UserStatics.Endpoint.USER_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetUserById() throws Exception {
        utils.setAuthorities("admin");
        UserDTO userDTO = UserDTO.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role("client")
                .build();
        when(userService.getById(anyLong())).thenReturn(userDTO);

        ResultActions response = mockMvc.perform(get(UserStatics.Endpoint.USER_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON));

        response
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(userDTO.getId().intValue())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userDTO.getEmail())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(userDTO.getPassword())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(userDTO.getVerified())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.loggedIn", CoreMatchers.is(userDTO.getLoggedIn())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(userDTO.getRole())));
    }

    @Test
    public void shouldThrowBadRequestExceptionInGetUserById() throws Exception {
        utils.setAuthorities("admin");
        when(userService.getById(anyLong())).thenThrow(new BadRequestException("message", 0));

        ResultActions response = mockMvc.perform(get(UserStatics.Endpoint.USER_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON));

        response
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateUserById() throws Exception {
        User user = utils.createUser();
        UserDTO userRequestDTO = UserDTO.builder()
                .password(user.getPassword())
                .role(user.getRole().getName().name())
                .build();
        UserDTO userResponseDTO = utils.createUserDTO(user);
        utils.setAuthorities(user.getRole().getName().name());

        when(userService.update(anyLong(), any(UserDTO.class))).thenReturn(userResponseDTO);
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/" + user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(userResponseDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(userResponseDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password",
                        CoreMatchers.is(userRequestDTO.getPassword())))
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
        when(userService.update(anyLong(), any(UserDTO.class))).thenThrow(new BadRequestException("message", 0));

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/" + user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDTO.builder().build())));

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
        when(userService.update(anyLong(), any(UserDTO.class))).thenThrow(new UnprocessableException("message", 0));

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/" + user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDTO.builder().build())));

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
        when(userService.update(anyLong(), any(UserDTO.class))).thenThrow(new ConflictException("message", 0));

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/" + user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDTO.builder().build())));

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

        ResultActions response = mockMvc.perform(delete(UserStatics.Endpoint.USER_PATH.concat("/" + user.getId()))
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

        ResultActions response = mockMvc.perform(delete(UserStatics.Endpoint.USER_PATH.concat("/" + user.getId()))
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

}
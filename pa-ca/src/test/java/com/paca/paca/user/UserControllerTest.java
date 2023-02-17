package com.paca.paca.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.user.controller.UserController;
import com.paca.paca.user.dto.UserDTO;
import com.paca.paca.user.dto.UserListDTO;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.user.service.UserService;
import com.paca.paca.user.statics.UserStatics;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserService userService;
    @MockBean private UserRepository userRepository;
    @MockBean private JwtService jwtService;

    @Test
    public void shouldGetUserList() throws Exception {
        UserDTO userDTO = UserDTO.builder()
                .id(1L).email("test@test.com").password("123456789aA#").verified(false).loggedIn(false).role("client")
                .build();

        ArrayList<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(userDTO);

        UserListDTO userListDTO = UserListDTO.builder().users(userDTOList).build();
        when(userService.getAll()).thenReturn(userListDTO);

        ResultActions response = mockMvc.perform(get(UserStatics.Endpoint.USER_PATH)
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetUserById() throws Exception {
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
        UserDTO userRequestDTO = UserDTO.builder()
                .password("123456789aB#").role("client")
                .build();
        UserDTO userResponseDTO = UserDTO.builder()
                .id(1L).email("test@test.com").password("123456789aB#").verified(false).loggedIn(false).role("client")
                .build();
        when(userService.update(anyLong(), any(UserDTO.class))).thenReturn(userResponseDTO);

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)));

        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(userResponseDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userResponseDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(userRequestDTO.getPassword())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(userResponseDTO.getVerified())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loggedIn", CoreMatchers.is(userResponseDTO.getLoggedIn())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role", CoreMatchers.is(userRequestDTO.getRole())));
    }

    @Test
    public void shouldThrowBadRequestExceptionInUpdateUserById() throws Exception {
        when(userService.update(anyLong(), any(UserDTO.class))).thenThrow(new BadRequestException("message", 0));

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDTO.builder().build())));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldThrowUnprocessableExceptionInUpdateUserById() throws Exception {
        when(userService.update(anyLong(), any(UserDTO.class))).thenThrow(new UnprocessableException("message", 0));

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/1"))
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
        when(userService.update(anyLong(), any(UserDTO.class))).thenThrow(new ConflictException("message", 0));

        ResultActions response = mockMvc.perform(patch(UserStatics.Endpoint.USER_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDTO.builder().build())));

        response
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteUserById() throws Exception {
        doNothing().when(userService).delete(anyLong());

        ResultActions response = mockMvc.perform(delete(UserStatics.Endpoint.USER_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldThrowBadRequestExceptionInDeleteUserById() throws Exception {
        doThrow(new BadRequestException("message", 0)).when(userService).delete(anyLong());

        ResultActions response = mockMvc.perform(delete(UserStatics.Endpoint.USER_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON));

        response
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

}
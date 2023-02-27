package com.paca.paca.client;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.client.service.ReviewService;
import com.paca.paca.client.statics.ClientStatics;
import com.paca.paca.client.statics.ReviewStatics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.client.controller.ClientController;
import com.paca.paca.client.controller.ReviewController;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { ClientController.class, ReviewController.class })
public class ClientControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ReviewService reviewService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetClientList() throws Exception {
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getAll()).thenReturn(clientListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(ClientStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(ClientStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetClientList() throws Exception {
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getAll()).thenReturn(clientListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSaveClient() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientService.save(any(ClientDTO.class))).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(post(ClientStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSaveClient() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientService.save(any(ClientDTO.class))).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(post(ClientStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSaveClient() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientService.save(any(ClientDTO.class))).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(post(ClientStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSaveClient() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO dto = utils.createClientDTO(client);

        when(clientService.save(any(ClientDTO.class))).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(post(ClientStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(dto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is(dto.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is(dto.getPhoneNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(dto.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stripeCustomerId",
                        CoreMatchers.is(dto.getStripeCustomerId())));
    }

    @Test
    public void shouldGetNoContentInGetClientById() throws Exception {
        when(clientService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetClientById() throws Exception {
        ClientDTO clientDTO = utils.createClientDTO(null);

        when(clientService.getById(anyLong())).thenReturn(clientDTO);

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + clientDTO.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(clientDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(clientDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(clientDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is(clientDTO.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is(clientDTO.getPhoneNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(clientDTO.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stripeCustomerId",
                        CoreMatchers.is(clientDTO.getStripeCustomerId())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInUpdateClientById() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);

        utils.setAuthorities("business");

        mockMvc.perform(put(ClientStatics.Endpoint.PATH.concat("/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInUpdateClientById() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);

        when(clientService.update(anyLong(), any(ClientDTO.class))).thenReturn(clientDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(put(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInUpdateClientById() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);

        when(clientService.update(anyLong(), any(ClientDTO.class))).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(put(ClientStatics.Endpoint.PATH.concat("/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateClientById() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);

        when(clientService.update(anyLong(), any(ClientDTO.class))).thenReturn(clientDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(put(ClientStatics.Endpoint.PATH.concat("/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(clientDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(clientDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(clientDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is(clientDTO.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is(clientDTO.getPhoneNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(clientDTO.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stripeCustomerId",
                        CoreMatchers.is(clientDTO.getStripeCustomerId())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteClientById() throws Exception {
        Client client = utils.createClient(null);

        utils.setAuthorities("business");

        mockMvc.perform(delete(ClientStatics.Endpoint.PATH.concat("/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeleteClientById() throws Exception {
        Client client = utils.createClient(null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(delete(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteClientById() throws Exception {
        Client client = utils.createClient(null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        doThrow(new NoContentException("message", 0)).when(clientService).delete(anyLong());

        utils.setAuthorities("client");

        mockMvc.perform(delete(ClientStatics.Endpoint.PATH.concat("/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteClientById() throws Exception {
        Client client = utils.createClient(null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        doNothing().when(clientService).delete(anyLong());

        utils.setAuthorities("client");

        mockMvc.perform(delete(ClientStatics.Endpoint.PATH.concat("/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGetNoContentInGetClientByUserId() throws Exception {
        when(clientService.getByUserId(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/user/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetClientByUserId() throws Exception {
        Client client = utils.createClient(null);
        ClientDTO clientDTO = utils.createClientDTO(client);

        when(clientService.getByUserId(anyLong())).thenReturn(clientDTO);

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/user/" + client.getUser().getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(clientDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(clientDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(clientDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is(clientDTO.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is(clientDTO.getPhoneNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", CoreMatchers.is(clientDTO.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stripeCustomerId",
                        CoreMatchers.is(clientDTO.getStripeCustomerId())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetAcceptedFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getAcceptedFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/accepted"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetAcceptedFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getAcceptedFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1) + "/friend/accepted"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetAcceptedFriends() throws Exception {
        Client client = utils.createClient(null);

        when(clientService.getAcceptedFriends(anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/accepted"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetAcceptedFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getAcceptedFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/accepted"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetRejectedFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getRejectedFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/rejected"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetRejectedFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getRejectedFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1) + "/friend/rejected"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetRejectedFriends() throws Exception {
        Client client = utils.createClient(null);

        when(clientService.getRejectedFriends(anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/rejected"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetRejectedFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getRejectedFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/rejected"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetPendingFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getPendingFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/pending"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetPendingFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getPendingFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1) + "/friend/pending"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetPendingFriends() throws Exception {
        Client client = utils.createClient(null);

        when(clientService.getPendingFriends(anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/pending"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetPendingFriends() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ClientDTO> clientDTOList = new ArrayList<>();
        clientDTOList.add(utils.createClientDTO(null));
        ClientListDTO clientListDTO = ClientListDTO.builder().clients(clientDTOList).build();

        when(clientService.getPendingFriends(anyLong())).thenReturn(clientListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/friend/pending"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clients", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSendFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.friendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("business");

        mockMvc.perform(post(
                ClientStatics.Endpoint.PATH.concat("/" + addresser.getId() + "/friend/pending/" + requester.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInSendFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.friendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("client");

        mockMvc.perform(post(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + (requester.getId() + 1)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSendFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        when(clientService.friendRequest(anyLong(), anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("client");

        mockMvc.perform(post(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSendFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        when(clientService.friendRequest(anyLong(), anyLong())).thenThrow(new ConflictException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("client");

        mockMvc.perform(post(
                ClientStatics.Endpoint.PATH.concat("/" + addresser.getId() + "/friend/pending/" + requester.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSendFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.friendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("client");

        mockMvc.perform(post(
                ClientStatics.Endpoint.PATH.concat("/" + addresser.getId() + "/friend/pending/" + requester.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requesterId",
                        CoreMatchers.is(dto.getRequesterId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addresserId",
                        CoreMatchers.is(dto.getAddresserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accepted", CoreMatchers.is(dto.getAccepted())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rejected", CoreMatchers.is(dto.getRejected())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        doNothing().when(clientService).deleteFriendRequest(anyLong(), anyLong());
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("business");

        mockMvc.perform(delete(
                ClientStatics.Endpoint.PATH.concat("/" + addresser.getId() + "/friend/pending/" + requester.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeleteFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        doNothing().when(clientService).deleteFriendRequest(anyLong(), anyLong());
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("client");

        mockMvc.perform(delete(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + (requester.getId() + 1)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        doThrow(new NoContentException("message", 0))
                .when(clientService).deleteFriendRequest(anyLong(), anyLong());
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));

        utils.setAuthorities("client");

        mockMvc.perform(delete(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(requester));
        doNothing().when(clientService).deleteFriendRequest(anyLong(), anyLong());

        utils.setAuthorities("client");

        mockMvc.perform(delete(
                ClientStatics.Endpoint.PATH.concat("/" + addresser.getId() + "/friend/pending/" + requester.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInAcceptFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.acceptFriendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("business");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/accept"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInAcceptFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.acceptFriendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + (addresser.getId() + 1) + "/friend/pending/" + requester.getId() + "/accept"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInAcceptFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        when(clientService.acceptFriendRequest(anyLong(), anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/accept"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInAcceptFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        when(clientService.acceptFriendRequest(anyLong(), anyLong())).thenThrow(new ConflictException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/accept"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldAcceptFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.acceptFriendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/accept"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requesterId",
                        CoreMatchers.is(dto.getRequesterId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addresserId",
                        CoreMatchers.is(dto.getAddresserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accepted", CoreMatchers.is(dto.getAccepted())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rejected", CoreMatchers.is(dto.getRejected())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInRejectFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.rejectFriendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("business");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/reject"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInRejectFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.rejectFriendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + (addresser.getId() + 1) + "/friend/pending/" + requester.getId() + "/reject"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInRejectFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        when(clientService.rejectFriendRequest(anyLong(), anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/reject"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInRejectFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);

        when(clientService.rejectFriendRequest(anyLong(), anyLong())).thenThrow(new ConflictException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/reject"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldRejectFriendRequest() throws Exception {
        Client addresser = utils.createClient(null);
        Client requester = utils.createClient(null);
        FriendDTO dto = utils.createFriendRequestDTO(null);

        when(clientService.rejectFriendRequest(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(addresser));

        utils.setAuthorities("client");

        mockMvc.perform(put(
                ClientStatics.Endpoint.PATH
                        .concat("/" + addresser.getId() + "/friend/pending/" + requester.getId() + "/reject"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requesterId",
                        CoreMatchers.is(dto.getRequesterId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.addresserId",
                        CoreMatchers.is(dto.getAddresserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accepted", CoreMatchers.is(dto.getAccepted())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rejected", CoreMatchers.is(dto.getRejected())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetReservations() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(clientService.getReservations(anyLong())).thenReturn(reservationListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetReservations() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(clientService.getReservations(anyLong())).thenReturn(reservationListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1) + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetReservations() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));

        when(clientService.getReservations(anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetReservations() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(clientService.getReservations(anyLong())).thenReturn(reservationListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/reservation"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetReservationsByDate() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(clientService.getReservationsByDate(anyLong(), any(Date.class))).thenReturn(reservationListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetReservationsByDate() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(clientService.getReservationsByDate(anyLong(), any(Date.class))).thenReturn(reservationListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1) + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetReservationsByDate() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));

        when(clientService.getReservationsByDate(anyLong(), any(Date.class)))
                .thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetReservationsByDate() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<ReservationDTO> reservationDTOList = new ArrayList<>();
        reservationDTOList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(reservationDTOList).build();

        when(clientService.getReservationsByDate(anyLong(), any(Date.class))).thenReturn(reservationListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/reservation/2000-01-01"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetFavoriteBranches() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<BranchDTO> branchDTOList = new ArrayList<>();
        branchDTOList.add(utils.createBranchDTO(null));
        BranchListDTO branchListDTO = BranchListDTO.builder().branches(branchDTOList).build();

        when(clientService.getFavoriteBranches(anyLong())).thenReturn(branchListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/favorite-branches"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInGetFavoriteBranches() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<BranchDTO> branchDTOList = new ArrayList<>();
        branchDTOList.add(utils.createBranchDTO(null));
        BranchListDTO branchListDTO = BranchListDTO.builder().branches(branchDTOList).build();

        when(clientService.getFavoriteBranches(anyLong())).thenReturn(branchListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + (client.getId() + 1) + "/favorite-branches"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInGetFavoriteBranches() throws Exception {
        Client client = utils.createClient(null);

        when(clientService.getFavoriteBranches(anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/favorite-branches"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetFavoriteBranches() throws Exception {
        Client client = utils.createClient(null);
        ArrayList<BranchDTO> branchDTOList = new ArrayList<>();
        branchDTOList.add(utils.createBranchDTO(null));
        BranchListDTO branchListDTO = BranchListDTO.builder().branches(branchDTOList).build();

        when(clientService.getFavoriteBranches(anyLong())).thenReturn(branchListDTO);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(get(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/favorite-branches"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.branches", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInAddFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(clientService.addFavoriteBranch(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(
                post(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInAddFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(clientService.addFavoriteBranch(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(
                post(ClientStatics.Endpoint.PATH
                        .concat("/" + (client.getId() + 1) + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInAddFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        when(clientService.addFavoriteBranch(anyLong(), anyLong())).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(
                post(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInAddFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        when(clientService.addFavoriteBranch(anyLong(), anyLong())).thenThrow(new ConflictException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(
                post(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldAddFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        BranchDTO dto = utils.createBranchDTO(branch);

        when(clientService.addFavoriteBranch(anyLong(), anyLong())).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(
                post(ClientStatics.Endpoint.PATH.concat("/" + client.getId() + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(branch.getId().intValue())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        doNothing().when(clientService).deleteFavoriteBranch(anyLong(), anyLong());
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("business");

        mockMvc.perform(
                delete(ClientStatics.Endpoint.PATH
                        .concat("/" + client.getId() + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeleteFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        doNothing().when(clientService).deleteFavoriteBranch(anyLong(), anyLong());
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(
                delete(ClientStatics.Endpoint.PATH
                        .concat("/" + (client.getId() + 1) + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        doThrow(new NoContentException("message", 0)).when(clientService).deleteFavoriteBranch(anyLong(), anyLong());
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(
                delete(ClientStatics.Endpoint.PATH
                        .concat("/" + client.getId() + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteFavoriteBranch() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);

        doNothing().when(clientService).deleteFavoriteBranch(anyLong(), anyLong());
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));

        utils.setAuthorities("client");

        mockMvc.perform(
                delete(ClientStatics.Endpoint.PATH
                        .concat("/" + client.getId() + "/favorite-branches/" + branch.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetReviewList() throws Exception {
        ArrayList<ReviewDTO> reviewDTOList = new ArrayList<>();
        reviewDTOList.add(utils.createReviewDTO(null));
        ReviewListDTO reviewListDTO = ReviewListDTO.builder().reviews(reviewDTOList).build();

        when(reviewService.getAll()).thenReturn(reviewListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(ReviewStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(ReviewStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetReviewList() throws Exception {
        ArrayList<ReviewDTO> reviewDTOList = new ArrayList<>();
        reviewDTOList.add(utils.createReviewDTO(null));
        ReviewListDTO reviewListDTO = ReviewListDTO.builder().reviews(reviewDTOList).build();

        when(reviewService.getAll()).thenReturn(reviewListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(ReviewStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reviews", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSaveReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.save(any(ReviewDTO.class))).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(post(ReviewStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSaveReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.save(any(ReviewDTO.class))).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(post(ReviewStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSaveReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.save(any(ReviewDTO.class))).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(post(ReviewStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSaveReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.save(any(ReviewDTO.class))).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(post(ReviewStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId",
                        CoreMatchers.is(dto.getClientId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(dto.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes", CoreMatchers.is(dto.getLikes())));
    }

    @Test
    public void shouldGetNoContentInGetReviewById() throws Exception {
        when(reviewService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(get(ReviewStatics.Endpoint.PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetReviewById() throws Exception {
        ReviewDTO reviewDTO = utils.createReviewDTO(null);

        when(reviewService.getById(anyLong())).thenReturn(reviewDTO);

        utils.setAuthorities("client");

        mockMvc.perform(get(ReviewStatics.Endpoint.PATH.concat("/" + reviewDTO.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(reviewDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId",
                        CoreMatchers.is(reviewDTO.getClientId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(reviewDTO.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(reviewDTO.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes", CoreMatchers.is(reviewDTO.getLikes())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInUpdateReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.update(anyLong(), any(ReviewDTO.class))).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("business");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInUpdateReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.update(anyLong(), any(ReviewDTO.class))).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(false);

        utils.setAuthorities("client");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInUpdateReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.update(anyLong(), any(ReviewDTO.class))).thenThrow(new NoContentException("message", 0));
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(reviewService.update(anyLong(), any(ReviewDTO.class))).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("client");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId",
                        CoreMatchers.is(dto.getClientId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(dto.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes", CoreMatchers.is(dto.getLikes())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(reviewService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeleteReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(false);
        doNothing().when(reviewService).delete(anyLong());

        utils.setAuthorities("client");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(true);
        doThrow(new NoContentException("message", 0)).when(reviewService).delete(anyLong());

        utils.setAuthorities("client");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteReviewById() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewRepository.existsByIdAndClientId(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(reviewService).delete(anyLong());

        utils.setAuthorities("client");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInLikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.like(anyLong(), anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInLikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.like(anyLong(), anyLong())).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + (client.getId() + 1)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInLikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.like(anyLong(), anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInLikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.like(anyLong(), anyLong())).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldLikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.like(anyLong(), anyLong())).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(put(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId",
                        CoreMatchers.is(dto.getClientId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(dto.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes", CoreMatchers.is(dto.getLikes())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDislikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.dislike(anyLong(), anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDislikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.dislike(anyLong(), anyLong())).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + (client.getId() + 1)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDislikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.dislike(anyLong(), anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInDislikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.dislike(anyLong(), anyLong())).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDislikeReview() throws Exception {
        Client client = utils.createClient(null);
        Review review = utils.createReview(client, null);
        ReviewDTO dto = utils.createReviewDTO(review);

        when(clientRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(client));
        when(reviewService.dislike(anyLong(), anyLong())).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(delete(ReviewStatics.Endpoint.PATH.concat("/" + review.getId() + "/client/" + client.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId",
                        CoreMatchers.is(dto.getClientId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", CoreMatchers.is(dto.getText())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes", CoreMatchers.is(dto.getLikes())));
    }

}

package com.paca.paca.reservation;

import com.paca.paca.exception.exceptions.BadRequestException;
import org.hamcrest.CoreMatchers;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.Client;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.controller.ReservationController;

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

import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { ReservationController.class })
public class ReservationControllerTest extends ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ReservationService reservationService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetReservationList() throws Exception {
        ArrayList<ReservationDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(dtoList).build();

        when(reservationService.getAll()).thenReturn(reservationListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(ReservationStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(ReservationStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetReservationList() throws Exception {
        ArrayList<ReservationDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createReservationDTO(null));
        ReservationListDTO reservationListDTO = ReservationListDTO.builder().reservations(dtoList).build();

        when(reservationService.getAll()).thenReturn(reservationListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(ReservationStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservations", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetNoContentInGetReservationById() throws Exception {
        when(reservationService.getById(anyLong())).thenThrow(new NoContentException("message", 0));
        mockMvc.perform(get(ReservationStatics.Endpoint.PATH.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetReservationById() throws Exception {
        ReservationDTO dto = utils.createReservationDTO(null);

        when(reservationService.getById(anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(get(ReservationStatics.Endpoint.PATH.concat("/" +
                dto.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestDate",
                        CoreMatchers.is(dto.getRequestDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationDate",
                        CoreMatchers.is(dto.getReservationDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientNumber",
                        CoreMatchers.is(dto.getClientNumber().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payment",
                        CoreMatchers.is(dto.getPayment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",
                        CoreMatchers.is(dto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payDate",
                        CoreMatchers.is(dto.getPayDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price",
                        CoreMatchers.is(dto.getPrice().doubleValue())));
    }

    @Test
    public void shouldGetNoContentInSaveReservation() throws Exception {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationService.save(any(ReservationDTO.class)))
                .thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(post(ReservationStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSaveReservation() throws Exception {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationService.save(any(ReservationDTO.class))).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(ReservationStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetBadRequestInSaveReservation() throws Exception {
        ReservationDTO dto = utils.createReservationDTO(null);

        when(reservationService.save(any(ReservationDTO.class))).thenThrow(new BadRequestException("message", 0));

        utils.setAuthorities("client");

        mockMvc.perform(post(ReservationStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSaveReservation() throws Exception {
        Reservation reservation = utils.createReservation(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationService.save(any(ReservationDTO.class))).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(post(ReservationStatics.Endpoint.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestDate",
                        CoreMatchers.is(dto.getRequestDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationDate",
                        CoreMatchers.is(dto.getReservationDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientNumber",
                        CoreMatchers.is(dto.getClientNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payment",
                        CoreMatchers.is(dto.getPayment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",
                        CoreMatchers.is(dto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payDate",
                        CoreMatchers.is(dto.getPayDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price",
                        CoreMatchers.is(dto.getPrice().doubleValue())));
    }

    @Test
    public void shouldGetNoContentInUpdateReservationById() throws Exception {
        Reservation reservation = utils.createReservation(null);
        Branch branch = utils.createBranch(null);
        ReservationDTO dto = utils.createReservationDTO(reservation);

        when(reservationService.update(anyLong(), any(ReservationDTO.class)))
                .thenThrow(new NoContentException("message", 0));
        when(branchRepository.findByBusiness_UserEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(branch));
        when(reservationRepository.existsByIdAndBranchId(anyLong(), anyLong())).thenReturn(true);

        utils.setAuthorities("admin");

        mockMvc.perform(put(ReservationStatics.Endpoint.PATH.concat("/" + reservation.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateReservationById() throws Exception {
        Client client = utils.createClient(null);
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch);
        ReservationDTO dto = utils.createReservationDTO(reservation);
        ClientGroup group = ClientGroup.builder().client(client).reservation(reservation).isOwner(true).build();

        when(reservationService.update(anyLong(),
                any(ReservationDTO.class))).thenReturn(dto);
        when(clientRepository.findByUserEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(client));
        when(clientGroupRepository.findByReservationIdAndClientId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.ofNullable(group));
        when(reservationRepository.existsByIdAndBranchId(anyLong(),
                anyLong())).thenReturn(true);

        utils.setAuthorities("admin");

        mockMvc.perform(put(ReservationStatics.Endpoint.PATH.concat("/" +
                reservation.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",
                        CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.branchId",
                        CoreMatchers.is(dto.getBranchId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestDate",
                        CoreMatchers.is(dto.getRequestDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationDate",
                        CoreMatchers.is(dto.getReservationDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientNumber",
                        CoreMatchers.is(dto.getClientNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payment",
                        CoreMatchers.is(dto.getPayment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",
                        CoreMatchers.is(dto.getStatus())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payDate",
                        CoreMatchers.is(dto.getPayDate().toInstant().toString().replace("Z", "+00:00"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price",
                        CoreMatchers.is(dto.getPrice().doubleValue())));
    }

    @Test
    public void shouldGetNoContentInDeleteReservationById() throws Exception {
        Reservation reservation = utils.createReservation(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findByBusiness_UserEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(branch));
        when(reservationRepository.existsByIdAndBranchId(anyLong(), anyLong())).thenReturn(true);
        doThrow(new NoContentException("message", 0)).when(reservationService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(ReservationStatics.Endpoint.PATH.concat("/" + reservation.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteReservationById() throws Exception {
        Reservation reservation = utils.createReservation(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findByBusiness_UserEmail(any(String.class)))
                .thenReturn(Optional.ofNullable(branch));
        when(reservationRepository.existsByIdAndBranchId(anyLong(), anyLong())).thenReturn(true);
        doNothing().when(reservationService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(ReservationStatics.Endpoint.PATH.concat("/" + reservation.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
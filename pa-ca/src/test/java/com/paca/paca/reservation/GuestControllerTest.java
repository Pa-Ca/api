package com.paca.paca.reservation;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.auth.ControllerTest;
import com.paca.paca.auth.service.JwtService;
import com.paca.paca.business.model.Business;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.dto.GuestListDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paca.paca.reservation.service.GuestService;
import com.paca.paca.reservation.controller.GuestController;
import com.paca.paca.reservation.statics.ReservationStatics;
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

import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = { GuestController.class })
public class GuestControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private GuestService guestService;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetGuestList() throws Exception {
        ArrayList<GuestDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createGuestDTO(null));
        GuestListDTO guestListDTO = GuestListDTO.builder().guests(dtoList).build();

        when(guestService.getAll()).thenReturn(guestListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(ReservationStatics.Endpoint.GUEST_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(ReservationStatics.Endpoint.GUEST_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetGuestList() throws Exception {
        ArrayList<GuestDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createGuestDTO(null));
        GuestListDTO guestListDTO = GuestListDTO.builder().guests(dtoList).build();

        when(guestService.getAll()).thenReturn(guestListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(ReservationStatics.Endpoint.GUEST_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.guests", CoreMatchers.hasItems()));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSaveGuest() throws Exception {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestService.save(any(GuestDTO.class))).thenReturn(dto);

        utils.setAuthorities("client");
        mockMvc.perform(post(ReservationStatics.Endpoint.GUEST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(post(ReservationStatics.Endpoint.GUEST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldSaveGuest() throws Exception {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestService.save(any(GuestDTO.class))).thenReturn(dto);

        utils.setAuthorities("admin");
        mockMvc.perform(post(ReservationStatics.Endpoint.GUEST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is(dto.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(dto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is(dto.getPhoneNumber())));
    }

    @Test
    public void shouldGetNoContentInGetGuestById() throws Exception {
        when(guestService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(get(ReservationStatics.Endpoint.GUEST_PATH.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetGuestById() throws Exception {
        GuestDTO dto = utils.createGuestDTO(null);

        when(guestService.getById(anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(get(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + dto.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is(dto.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(dto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is(dto.getPhoneNumber())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInUpdateGuestById() throws Exception {
        Guest guest = utils.createGuest();
        GuestDTO dto = utils.createGuestDTO(guest);

        utils.setAuthorities("client");
        mockMvc.perform(put(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(put(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInUpdateGuestById() throws Exception {
        Guest guest = utils.createGuest();
        Business business = utils.createBusiness(null);
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestService.update(anyLong(), any(GuestDTO.class))).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));

        utils.setAuthorities("admin");

        mockMvc.perform(put(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateGuestById() throws Exception {
        Guest guest = utils.createGuest();
        Business business = utils.createBusiness(null);
        GuestDTO dto = utils.createGuestDTO(guest);

        when(guestService.update(anyLong(), any(GuestDTO.class))).thenReturn(dto);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));

        utils.setAuthorities("admin");

        mockMvc.perform(put(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is(dto.getSurname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(dto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is(dto.getPhoneNumber())));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteGuestById() throws Exception {
        Guest guest = utils.createGuest();
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));

        utils.setAuthorities("client");
        mockMvc.perform(delete(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(delete(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteGuestById() throws Exception {
        Guest guest = utils.createGuest();
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        doThrow(new NoContentException("message", 0)).when(guestService).delete(anyLong());

        utils.setAuthorities("admin");

        mockMvc.perform(delete(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteGuestById() throws Exception {
        Guest guest = utils.createGuest();
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        doNothing().when(guestService).delete(anyLong());

        utils.setAuthorities("admin");

        mockMvc.perform(delete(ReservationStatics.Endpoint.GUEST_PATH.concat("/" + guest.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

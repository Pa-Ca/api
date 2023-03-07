package com.paca.paca.business;

        import com.paca.paca.branch.dto.BranchDTO;
        import com.paca.paca.branch.model.Branch;
        import com.paca.paca.branch.statics.BranchStatics;
        import com.paca.paca.business.dto.BusinessDTO;
        import com.paca.paca.business.model.Business;
        import com.paca.paca.business.statics.BusinessStatics;
        import com.paca.paca.business.dto.BusinessDTO;
        import com.paca.paca.business.model.Business;
        import com.paca.paca.business.statics.BusinessStatics;
        import com.paca.paca.business.statics.BusinessStatics;
        import com.paca.paca.business.controller.BusinessController;
        import com.paca.paca.business.dto.BusinessDTO;
        import com.paca.paca.business.dto.BusinessListDTO;
        import com.paca.paca.business.service.BusinessService;
        import com.paca.paca.business.statics.BusinessStatics;
        import com.paca.paca.utils.TestUtils;
        import com.paca.paca.auth.ControllerTest;
        import com.paca.paca.client.model.Client;
        import com.paca.paca.client.model.Review;
        import com.paca.paca.business.model.Business;
        import com.paca.paca.client.dto.ClientDTO;
        import com.paca.paca.client.dto.FriendDTO;
        import com.paca.paca.client.dto.ReviewDTO;
        import com.paca.paca.business.dto.BusinessDTO;
        import com.paca.paca.auth.service.JwtService;
        import com.paca.paca.client.dto.ReviewListDTO;
        import com.paca.paca.business.dto.BusinessListDTO;
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
@WebMvcTest(controllers = { BusinessController.class })
public class BusinessControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BusinessService businessService;

    private TestUtils utils = TestUtils.builder().build();

    //Get All
    @Test
    public void shouldGetForbiddenDueToInvalidRoleInGetBusinessList() throws Exception {
        ArrayList<BusinessDTO> businessDTOList = new ArrayList<>();
        businessDTOList.add(utils.createBusinessDTO(null));
        BusinessListDTO businessListDTO = BusinessListDTO.builder().business(businessDTOList).build();

        when(businessService.getAll()).thenReturn(businessListDTO);

        utils.setAuthorities("client");
        mockMvc.perform(get(BusinessStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));

        utils.setAuthorities("business");
        mockMvc.perform(get(BusinessStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetBusinessList() throws Exception {
        ArrayList<BusinessDTO> dtoList = new ArrayList<>();
        dtoList.add(utils.createBusinessDTO(null));
        BusinessListDTO businessListDTO = BusinessListDTO.builder().business(dtoList).build();

        when(businessService.getAll()).thenReturn(businessListDTO);

        utils.setAuthorities("admin");

        mockMvc.perform(get(BusinessStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.business", CoreMatchers.hasItems()));
    }

    //Save
    @Test
    public void shouldGetForbiddenDueToInvalidRoleInSaveBusiness() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessService.save(any(BusinessDTO.class))).thenReturn(dto);

        utils.setAuthorities("client");

        mockMvc.perform(post(BusinessStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInSaveBusiness() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessService.save(any(BusinessDTO.class))).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(BusinessStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetConflictInSaveBusiness() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessService.save(any(BusinessDTO.class))).thenThrow(new ConflictException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(post(BusinessStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldSaveBusiness() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessService.save(any(BusinessDTO.class))).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(post(BusinessStatics.Endpoint.PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(dto.getUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(dto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(dto.getVerified())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is(dto.getTier())));
    }

    //GetByID
    @Test
    public void shouldGetNoContentInGetBusinessById() throws Exception {
        when(businessService.getById(anyLong())).thenThrow(new NoContentException("message", 0));

        utils.setAuthorities("business");

        mockMvc.perform(get(BusinessStatics.Endpoint.PATH.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldGetBusinessById() throws Exception {
        BusinessDTO dto = utils.createBusinessDTO(null);

        when(businessService.getById(anyLong())).thenReturn(dto);

        utils.setAuthorities("business");

        mockMvc.perform(get(BusinessStatics.Endpoint.PATH.concat("/" + dto.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(dto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(dto.getUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(dto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(dto.getVerified())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is(dto.getTier())));
    }

    //UpdateById
    @Test
    public void shouldGetForbiddenDueToInvalidRoleInUpdateBusinessById() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO businessDTO = utils.createBusinessDTO(business);

        utils.setAuthorities("client");

        mockMvc.perform(put(BusinessStatics.Endpoint.PATH.concat("/" + business.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(businessDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInUpdateBusinessById() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO businessDTO = utils.createBusinessDTO(business);

        when(businessService.update(anyLong(), any(BusinessDTO.class))).thenReturn(businessDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));

        utils.setAuthorities("business");

        mockMvc.perform(put(BusinessStatics.Endpoint.PATH.concat("/" + (business.getId() + 1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(businessDTO)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInUpdateBusinessById() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO businessDTO = utils.createBusinessDTO(business);

        when(businessService.update(anyLong(), any(BusinessDTO.class))).thenThrow(new NoContentException("message", 0));
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));

        utils.setAuthorities("business");

        mockMvc.perform(put(BusinessStatics.Endpoint.PATH.concat("/" + business.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(businessDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldUpdateBusinessById() throws Exception {
        Business business = utils.createBusiness(null);
        BusinessDTO businessDTO = utils.createBusinessDTO(business);

        when(businessService.update(anyLong(), any(BusinessDTO.class))).thenReturn(businessDTO);
        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));

        utils.setAuthorities("business");

        mockMvc.perform(put(BusinessStatics.Endpoint.PATH.concat("/" + business.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(businessDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(businessDTO.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(businessDTO.getUserId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(businessDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(businessDTO.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.verified", CoreMatchers.is(businessDTO.getVerified())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tier", CoreMatchers.is(businessDTO.getTier())));
    }

    //Delete
    @Test
    public void shouldGetForbiddenDueToInvalidRoleInDeleteBusinessById() throws Exception {
        Business business = utils.createBusiness(null);

        utils.setAuthorities("client");

        mockMvc.perform(delete(BusinessStatics.Endpoint.PATH.concat("/" + business.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetForbiddenDueToInvalidUserInDeleteBusinessById() throws Exception {
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));

        utils.setAuthorities("business");

        mockMvc.perform(delete(BusinessStatics.Endpoint.PATH.concat("/" + (business.getId() + 1)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Unauthorized access for this operation")));
    }

    @Test
    public void shouldGetNoContentInDeleteBusinessById() throws Exception {
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        doThrow(new NoContentException("message", 0)).when(businessService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(BusinessStatics.Endpoint.PATH.concat("/" + business.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("message")));
    }

    @Test
    public void shouldDeleteBusinessById() throws Exception {
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(business));
        doNothing().when(businessService).delete(anyLong());

        utils.setAuthorities("business");

        mockMvc.perform(delete(BusinessStatics.Endpoint.PATH.concat("/" + business.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
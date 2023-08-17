package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.utils.BranchMapperImpl;
import com.paca.paca.branch.utils.AmenityMapperImpl;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
public class BranchMapperTest {

    @InjectMocks
    private BranchMapperImpl branchMapper;

    @InjectMocks
    private AmenityMapperImpl amenityMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapBranchEntityToBranchDTO() {
        Branch branch = utils.createBranch(null);

        BranchDTO response = branchMapper.toDTO(branch);
        BranchDTO expected = new BranchDTO(
                branch.getId(),
                branch.getBusiness().getId(),
                branch.getName(),
                branch.getScore(),
                branch.getCapacity(),
                branch.getReservationPrice(),
                branch.getMapsLink(),
                branch.getLocation(),
                branch.getOverview(),
                branch.getVisibility(),
                branch.getReserveOff(),
                branch.getPhoneNumber(),
                branch.getType(),
                branch.getHourIn(),
                branch.getHourOut(),
                branch.getAverageReserveTime(),
                branch.getDollarExchange(),
                branch.getDeleted());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapBranchDTOtoBranchEntity() {
        Business business = utils.createBusiness(null);
        BranchDTO dto = utils.createBranchDTO(utils.createBranch(business));

        Branch response = branchMapper.toEntity(dto, business);
        Branch expected = new Branch(
                dto.getId(),
                business,
                dto.getName(),
                dto.getScore(),
                dto.getCapacity(),
                dto.getReservationPrice(),
                dto.getMapsLink(),
                dto.getLocation(),
                dto.getOverview(),
                dto.getVisibility(),
                dto.getReserveOff(),
                dto.getPhoneNumber(),
                dto.getType(),
                dto.getHourIn(),
                dto.getHourOut(),
                dto.getAverageReserveTime(),
                dto.getDollarExchange(),
                dto.getDeleted());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapBranchDTOtoBranchEntity() {
        Branch branch = utils.createBranch(null);

        // Not changing ID
        BranchDTO dto = BranchDTO.builder()
                .id(branch.getId() + 1)
                .build();
        Branch updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getId()).isEqualTo(branch.getId());

        // Not changing Business ID
        dto = BranchDTO.builder()
                .businessId(branch.getBusiness().getId() + 1)
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getBusiness().getId()).isEqualTo(branch.getBusiness().getId());

        // Changing location
        dto = BranchDTO.builder()
                .location("m_location")
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getLocation()).isEqualTo(dto.getLocation());

        // Changing mapsLink
        dto = BranchDTO.builder()
                .mapsLink("m_mapsLink")
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getMapsLink()).isEqualTo(dto.getMapsLink());

        // Changing name
        dto = BranchDTO.builder()
                .name("m_test")
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getName()).isEqualTo(dto.getName());

        // Changing overview
        dto = BranchDTO.builder()
                .overview("m_overview")
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getOverview()).isEqualTo(dto.getOverview());

        // Changing score
        dto = BranchDTO.builder()
                .score(branch.getScore() + 0.5f)
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getScore()).isEqualTo(dto.getScore());

        // Changing capacity
        dto = BranchDTO.builder()
                .capacity((short) (branch.getCapacity() + 1))
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getCapacity()).isEqualTo(dto.getCapacity());

        // Changing reservation price
        dto = BranchDTO.builder()
                .reservationPrice(branch.getReservationPrice().add(BigDecimal.valueOf(1L)))
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getReservationPrice()).isEqualTo(dto.getReservationPrice());

        // Changing reservation off
        dto = BranchDTO.builder()
                .reserveOff(!branch.getReserveOff())
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getReserveOff()).isEqualTo(dto.getReserveOff());

        // Changing average reserve time
        dto = BranchDTO.builder()
                .averageReserveTime(branch.getAverageReserveTime().plusMinutes(1))
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getAverageReserveTime()).isEqualTo(dto.getAverageReserveTime());

        // Changing visibility
        dto = BranchDTO.builder()
                .visibility(!branch.getVisibility())
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getVisibility()).isEqualTo(dto.getVisibility());

        // Changing phone number
        dto = BranchDTO.builder()
                .phoneNumber(branch.getPhoneNumber() + ".")
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());

        // Changing type
        dto = BranchDTO.builder()
                .type(branch.getType() + ".")
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getType()).isEqualTo(dto.getType());

        // Changing hour in
        dto = BranchDTO.builder()
                .hourIn(branch.getHourIn().plusMinutes(1))
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getHourIn()).isEqualTo(dto.getHourIn());

        // Changing hour out
        dto = BranchDTO.builder()
                .hourOut(branch.getHourOut().plusMinutes(1))
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getHourOut()).isEqualTo(dto.getHourOut());

        // Changing dollar exchange
        dto = BranchDTO.builder()
                .dollarExchange(branch.getDollarExchange() + 1F)
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getDollarExchange()).isEqualTo(dto.getDollarExchange());

        // Changing deleted
        dto = BranchDTO.builder()
                .deleted(!branch.getDeleted())
                .build();
        updatedBranch = branchMapper.updateModel(dto, branch);
        assertThat(updatedBranch).isNotNull();
        assertThat(updatedBranch.getDeleted()).isEqualTo(dto.getDeleted());
    }
}

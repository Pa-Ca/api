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
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        BranchDTO dto = new BranchDTO(
                branch.getId() + 1,
                business.getId() + 1,
                branch.getName() + ".",
                branch.getScore() + 1,
                (short) (branch.getCapacity() + 1),
                branch.getReservationPrice().add(BigDecimal.ONE),
                branch.getMapsLink() + ".",
                branch.getLocation() + ".",
                branch.getOverview() + ".",
                !branch.getVisibility(),
                !branch.getReserveOff(),
                branch.getPhoneNumber() + ".",
                branch.getType() + ".",
                branch.getHourIn().plusMinutes(1),
                branch.getHourOut().plusMinutes(1),
                branch.getAverageReserveTime().plusMinutes(1),
                branch.getDollarExchange() + 1,
                !branch.getDeleted());
        Branch response = branchMapper.updateModel(dto, branch);
        Branch expected = new Branch(
                branch.getId(),
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
}

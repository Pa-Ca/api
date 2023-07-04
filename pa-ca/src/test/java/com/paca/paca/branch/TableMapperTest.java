package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
// Import Table Model and DTO
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.utils.TableMapperImpl;
import com.paca.paca.branch.dto.TableDTO;
// Import the TableMapper



import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)
public class TableMapperTest {
    
    @InjectMocks
    private TableMapperImpl tableMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapTableEntityToTableDTO(){
    
        Table table = utils.createTable(null);

        TableDTO tableDTO = tableMapper.toDTO(table);

        // Check if the tableDTO is not null
        assertThat(tableDTO).isNotNull();
        // Check all the attributes of the tableDTO
        assertThat(tableDTO.getId()).isEqualTo(table.getId());
        assertThat(tableDTO.getBranchId()).isEqualTo(table.getBranch().getId());
        assertThat(tableDTO.getName()).isEqualTo(table.getName());
    }

    @Test
    void shouldMapTableDTOtoTableEntity(){

        // Create a branch
        Branch branch = utils.createBranch(null);

        TableDTO tableDTO = utils.createTableDTO(branch);

        Table table = tableMapper.toEntity(tableDTO, branch);

        // Check if the table is not null
        assertThat(table).isNotNull();

        // Check tha all the attributes of the table are equal to the attributes of the tableDTO
        assertThat(table.getId()).isEqualTo(tableDTO.getId());
        assertThat(table.getName()).isEqualTo(tableDTO.getName());
        assertThat(table.getBranch().getId()).isEqualTo(tableDTO.getBranchId());
    }

    @Test
    void shouldPartiallyMapTableDTOtoTableEntity(){
        // Create a branch
        Branch branch = utils.createBranch(null);

        // Create a table DTO
        TableDTO tableDTO = utils.createTableDTO(branch);

        // Create a table from the tableDTO
        Table table = tableMapper.toEntity(tableDTO, branch);

        // Change the TableDTO: branchId to the branch id + 1
        tableDTO.setBranchId(branch.getId() + 1);
        // Change the table name
        tableDTO.setName("New Name");
        // Change the table to deleted
        tableDTO.setDeleted(!tableDTO.isDeleted());

        // Change the Id of the tableDTO to the id of the table +1
        tableDTO.setId(table.getId() + 1);

        // Update the table with the new tableDTO
        table = tableMapper.updateModel(tableDTO, table);

        // Check that the ID and the branchId are not updated
        assertThat(table.getId()).isNotEqualTo(tableDTO.getId());
        assertThat(table.getBranch().getId()).isNotEqualTo(tableDTO.getBranchId());

        // Check that the name is updated
        assertThat(table.getName()).isEqualTo(tableDTO.getName());
        // Check that the deleted is updated
        assertThat(table.isDeleted()).isEqualTo(tableDTO.isDeleted());
    }

}

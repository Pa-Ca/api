package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.utils.TableMapperImpl;

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
    void shouldMapTableEntityToTableDTO() {
        Table table = utils.createTable(null);

        TableDTO response = tableMapper.toDTO(table);
        TableDTO expected = new TableDTO(
                table.getId(),
                table.getBranch().getId(),
                table.getName());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapTableDTOtoTableEntity() {
        Branch branch = utils.createBranch(null);
        TableDTO tableDTO = utils.createTableDTO(branch);

        Table table = tableMapper.toEntity(tableDTO, branch);
        Table expected = new Table(
                tableDTO.getId(),
                branch,
                tableDTO.getName());

        assertThat(table).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapTableDTOtoTableEntity() {
        Table table = utils.createTable(null);

        // Not changing ID
        TableDTO dto = TableDTO.builder()
                .id(table.getId() + 1)
                .build();
        Table updatedTable = tableMapper.updateModel(dto, table);
        assertThat(updatedTable).isNotNull();
        assertThat(updatedTable.getId()).isEqualTo(table.getId());

        // Not changing branch
        dto = TableDTO.builder()
                .branchId(table.getBranch().getId() + 1)
                .build();
        updatedTable = tableMapper.updateModel(dto, table);
        assertThat(updatedTable).isNotNull();
        assertThat(updatedTable.getBranch().getId()).isEqualTo(table.getBranch().getId());

        // Changing name
        dto = TableDTO.builder()
                .name(table.getName() + "a")
                .build();
        updatedTable = tableMapper.updateModel(dto, table);
        assertThat(updatedTable).isNotNull();
        assertThat(updatedTable.getName()).isEqualTo(dto.getName());
    }

}

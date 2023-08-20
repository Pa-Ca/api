package com.paca.paca.branch;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.model.Branch;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TableRepositoryTest extends RepositoryTest {

    @Test
    void shouldFindAllByBranchId() {
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < 10; i++) {
            Table table = Table.builder()
                    .branch(branch)
                    .name("Mesa " + i)
                    .build();

            tableRepository.save(table);
        }

        List<Table> tables = tableRepository.findAllByBranchId(branch.getId());
        assertThat(tables.size()).isEqualTo(10);
    }

    @Test
    void shouldCheckThatExistsByBranchIdAndName() {
        Branch branch = utils.createBranch(null);

        Table table = Table.builder()
                .branch(branch)
                .name("Mesa 1")
                .build();

        tableRepository.save(table);

        assertThat(tableRepository.existsByBranchIdAndName(branch.getId(), "Mesa 1")).isTrue();
    }

    @Test
    void shouldCheckThatDoesNotExistsByBranchIdAndName() {
        Branch branch = utils.createBranch(null);

        Table table = Table.builder()
                .branch(branch)
                .name("Mesa 1")
                .build();

        tableRepository.save(table);

        assertThat(tableRepository.existsByBranchIdAndName(branch.getId(), "Mesa 2")).isFalse();
    }

    @Test
    void shouldCheckThatExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);

        Table table = Table.builder()
                .branch(branch)
                .name("Mesa 1")
                .build();

        Table savedTable = tableRepository.save(table);

        assertThat(tableRepository.existsByIdAndBranch_Business_Id(savedTable.getId(), branch.getBusiness().getId()))
                .isTrue();
    }

    @Test
    void shouldCheckThatDoesNotExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);

        Table table = Table.builder()
                .branch(branch)
                .name("Mesa 1")
                .build();

        Table savedTable = tableRepository.save(table);

        assertThat(
                tableRepository.existsByIdAndBranch_Business_Id(savedTable.getId(), branch.getBusiness().getId() + 1))
                .isFalse();
    }
}

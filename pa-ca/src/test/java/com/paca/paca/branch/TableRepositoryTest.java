package com.paca.paca.branch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.business.repository.BusinessRepository;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TableRepositoryTest extends PacaTest {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .businessRepository(businessRepository)
                .branchRepository(branchRepository)
                .tableRepository(tableRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        branchRepository.deleteAll();
        tableRepository.deleteAll();

    }

    @AfterEach
    void restoreTest() {
        tableRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
        businessRepository.deleteAll();
        branchRepository.deleteAll();

    }

    @Test
    void shouldCreateTable() {
        // Arrange
        Branch branch = utils.createBranch(null);
        Table table = Table.builder()
                .name("Mesa que mas aplaude")
                .branch(branch)
                .build();

        // Act
        Table savedTable = tableRepository.save(table);

        // Assert
        assertThat(savedTable).isNotNull();
        assertThat(savedTable.getId()).isEqualTo(table.getId());
        assertThat(savedTable.getBranch()).isEqualTo(branch);
        assertThat(savedTable.getName()).isEqualTo("Mesa que mas aplaude");
    }

    @Test
    void shouldGetAllTables() {
        // Arrange
        Branch branch = utils.createBranch(null);
        Table table1 = Table.builder()
                .branch(branch)
                .name("Mesa 1")
                .build();
        Table table2 = Table.builder()
                .branch(branch)
                .name("Mesa 2")
                .build();
        Table table3 = Table.builder()
                .branch(branch)
                .name("Mesa 3")
                .build();
        tableRepository.save(table1);
        tableRepository.save(table2);
        tableRepository.save(table3);

        // Act
        List<Table> tables = tableRepository.findAll();

        // Assert
        assertThat(tables).isNotNull();
        assertThat(tables.size()).isEqualTo(3);
        assertThat(tables.get(0)).isEqualTo(table1);
        assertThat(tables.get(1)).isEqualTo(table2);
        assertThat(tables.get(2)).isEqualTo(table3);
    }

    @Test
    void shouldFindAllByBranchIdAndDeletedFalse() {
        // Create two branches
        Branch branch1 = utils.createBranch(null);
        Branch branch2 = utils.createBranch(null);

        // Create three tables for branch1 and 2 for branch2
        Table table1 = Table.builder()
                .branch(branch1)
                .name("Mesa 1")
                .build();
        Table table2 = Table.builder()
                .branch(branch1)
                .name("Mesa 2")
                .deleted(true)
                .build();

        Table table3 = Table.builder()
                .branch(branch1)
                .name("Mesa 3")
                .build();

        Table table4 = Table.builder()
                .branch(branch2)
                .name("Mesa 4")
                .deleted(true)
                .build();

        Table table5 = Table.builder()
                .branch(branch2)
                .name("Mesa 5")
                .build();

        List<Table> tables = List.of(table1, table2, table3, table4, table5);

        // Save all tables
        tableRepository.saveAll(tables);

        // Get all tables for branch1 that are not deleted
        List<Table> tablesForBranch1 = tableRepository.findAllByBranchIdAndDeletedFalse(branch1.getId());

        // Do the proccess manually
        List<Table> expectedTablesForBranch1 = List.of(table1, table3);

        // Assert
        assertThat(tablesForBranch1).isNotNull();
        assertThat(tablesForBranch1.size()).isEqualTo(2);
        assertThat(tablesForBranch1).isEqualTo(expectedTablesForBranch1);

        // Now get all tables for branch2 that are not deleted
        List<Table> tablesForBranch2 = tableRepository.findAllByBranchIdAndDeletedFalse(branch2.getId());

        // Do the proccess manually
        List<Table> expectedTablesForBranch2 = List.of(table5);

        // Assert
        assertThat(tablesForBranch2).isNotNull();
        assertThat(tablesForBranch2.size()).isEqualTo(1);
        assertThat(tablesForBranch2).isEqualTo(expectedTablesForBranch2);
    }

    @Test
    void shouldVerifyIfExistsByBranchIdAndName() {
        // Create a branch
        Branch branch = utils.createBranch(null);

        // Create a table
        Table table = Table.builder()
                .branch(branch)
                .name("Mesa 1")
                .build();

        // Save table
        tableRepository.save(table);

        // Assert
        assertThat(tableRepository.existsByBranchIdAndName(branch.getId(), "Mesa 1")).isTrue();
        assertThat(tableRepository.existsByBranchIdAndName(branch.getId(), "Mesa 2")).isFalse();
        assertThat(tableRepository.existsByBranchIdAndName(branch.getId() + 1, "Mesa 1")).isFalse();
    }
}

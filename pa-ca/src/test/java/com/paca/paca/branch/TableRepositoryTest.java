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
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
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

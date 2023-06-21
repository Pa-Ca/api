package com.paca.paca.branch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Table;
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
    void shouldGetAllTables(){
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
}

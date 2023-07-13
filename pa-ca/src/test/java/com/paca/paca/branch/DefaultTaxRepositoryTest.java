package com.paca.paca.branch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
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
public class DefaultTaxRepositoryTest extends PacaTest {
    
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private DefaultTaxRepository defaultTaxRepository;

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
                .defaultTaxRepository(defaultTaxRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        branchRepository.deleteAll();
        defaultTaxRepository.deleteAll();
        
    }

    @AfterEach
    void restoreTest() {
        defaultTaxRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
        businessRepository.deleteAll();
        branchRepository.deleteAll();
        
    }

    @Test
    void shouldCreateDefaultTax() {
        // Arrange
        Branch branch = utils.createBranch(null);
        DefaultTax defaultTax = DefaultTax.builder()
                                .branch(branch)
                                .name("VAT")
                                .type(1)
                                .value(10.0F)
                                .build();

            
        // Act
        DefaultTax savedDefaultTax = defaultTaxRepository.save(defaultTax);

        // Assert
        assertThat(savedDefaultTax).isNotNull();
        assertThat(savedDefaultTax.getId()).isEqualTo(defaultTax.getId());
        assertThat(savedDefaultTax.getBranch()).isEqualTo(branch);
        assertThat(savedDefaultTax.getName()).isEqualTo("VAT");
        assertThat(savedDefaultTax.getType()).isEqualTo(1);
        assertThat(savedDefaultTax.getValue()).isEqualTo(10.0F);
    }

    @Test 
    void shouldGetAllDefaultTaxes(){
        // Arrange
        Branch branch = utils.createBranch(null);
        DefaultTax defaultTax1 = DefaultTax.builder()
                                .branch(branch)
                                .name("VAT")
                                .type(1)
                                .value(10.0F)
                                .build();
        DefaultTax defaultTax2 = DefaultTax.builder()
                                .branch(branch)
                                .name("IVA")
                                .type(0)
                                .value(15.0F)
                                .build();
        DefaultTax defaultTax3 = DefaultTax.builder()
                                .branch(branch)
                                .name("VATB")
                                .type(1)
                                .value(16.0F)
                                .build();
        defaultTaxRepository.save(defaultTax1);
        defaultTaxRepository.save(defaultTax2);
        defaultTaxRepository.save(defaultTax3);

        // Act
        List<DefaultTax> defaultTaxes = defaultTaxRepository.findAll();

        // Assert
        assertThat(defaultTaxes).isNotNull();
        assertThat(defaultTaxes.size()).isEqualTo(3);
        assertThat(defaultTaxes.get(0)).isEqualTo(defaultTax1);
        assertThat(defaultTaxes.get(1)).isEqualTo(defaultTax2);
        assertThat(defaultTaxes.get(2)).isEqualTo(defaultTax3);
    }
}

package com.paca.paca.branch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.sale.model.Tax;
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
    void shouldGetAllDefaultTaxesByBranchId() {
        int nTaxes = 10;
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < nTaxes; i++) {
            Tax tax = utils.createTax();
            defaultTaxRepository.save(DefaultTax.builder()
                    .branch(branch)
                    .tax(tax)
                    .build());
            utils.createTax();
        }

        List<Tax> taxes = defaultTaxRepository.findAllByBranchId(branch.getId());

        assertThat(taxes.size()).isEqualTo(nTaxes);
    }

    @Test
    void shouldCheckThatDefaultTaxExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        Tax tax = utils.createTax();
        DefaultTax defaultTax = defaultTaxRepository.save(DefaultTax.builder()
                .branch(branch)
                .tax(tax)
                .build());

        Boolean exists = defaultTaxRepository.existsByIdAndBranch_Business_Id(defaultTax.getId(),
                branch.getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatDefaultTaxDoesNotExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        Tax tax = utils.createTax();
        DefaultTax defaultTax = defaultTaxRepository.save(DefaultTax.builder()
                .branch(branch)
                .tax(tax)
                .build());

        Boolean exists = defaultTaxRepository.existsByIdAndBranch_Business_Id(defaultTax.getId(),
                branch.getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }

}

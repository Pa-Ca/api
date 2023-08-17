package com.paca.paca.branch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.business.model.Business;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;

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
public class BranchRepositoryTest extends PacaTest {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private BranchAmenityRepository branchAmenityRepository;

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
                .amenityRepository(amenityRepository)
                .branchAmenityRepository(branchAmenityRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        branchAmenityRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        branchAmenityRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldGetAllByBusinessId() {
        int nBranches = 10;
        Business business = utils.createBusiness(null);

        for (int i = 0; i < nBranches; i++) {
            utils.createBranch(business);
            utils.createBranch(null);
        }

        List<Branch> branches = branchRepository.findAllByBusinessId(business.getId());

        assertThat(branches.size()).isEqualTo(nBranches);
    }

    @Test
    void shouldCheckThatBranchExistsByIdAndBusinessId() {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        assertThat(branchRepository.existsByIdAndBusinessId(branch.getId(), business.getId())).isTrue();
    }

    @Test
    void shouldCheckThatBranchDoesNotExistsByIdAndBusinessId() {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        assertThat(branchRepository.existsByIdAndBusinessId(branch.getId(), business.getId() + 1)).isFalse();
    }
}

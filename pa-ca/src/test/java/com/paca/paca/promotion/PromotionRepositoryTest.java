package com.paca.paca.promotion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.promotion.model.Promotion;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.promotion.repository.PromotionRepository;

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
public class PromotionRepositoryTest extends PacaTest {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PromotionRepository promotionRepository;

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
                .promotionRepository(promotionRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        promotionRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        promotionRepository.deleteAll();
        branchRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldGetAllPromotionsByBranchId() {
        int nPromotions = 10;
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < nPromotions; i++) {
            utils.createPromotion(branch);
            utils.createPromotion(null);
        }

        List<Promotion> promotions = promotionRepository.findAllByBranchId(branch.getId());

        assertThat(promotions.size()).isEqualTo(nPromotions);
    }

    @Test
    void shouldCheckThatPromotionExistsByIdAndBranch_Business_Id() {
        Promotion promotion = utils.createPromotion(null);

        boolean expected = promotionRepository.existsByIdAndBranch_Business_Id(promotion.getId(),
                promotion.getBranch().getBusiness().getId());

        assertThat(expected).isTrue();
    }

    @Test
    void shouldCheckThatPromotionDoesNotExistsByIdAndBranch_Business_Id() {
        Promotion promotion = utils.createPromotion(null);

        boolean expected = promotionRepository.existsByIdAndBranch_Business_Id(promotion.getId(),
                promotion.getBranch().getBusiness().getId() + 1);

        assertThat(expected).isFalse();
    }
}

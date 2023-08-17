package com.paca.paca.branch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
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
public class PaymentOptionRepositoryTest extends PacaTest {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private PaymentOptionRepository paymentOptionRepository;

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
                .paymentOptionRepository(paymentOptionRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        branchRepository.deleteAll();
        paymentOptionRepository.deleteAll();

    }

    @AfterEach
    void restoreTest() {
        paymentOptionRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
        businessRepository.deleteAll();
        branchRepository.deleteAll();

    }

    @Test
    void shouldCreatePaymentOption() {
        // Arrange
        Branch branch = utils.createBranch(null);
        PaymentOption paymentOption = PaymentOption.builder()
                .branch(branch)
                .name("VAT")
                .description("Descripcion")
                .build();

        // Act
        PaymentOption savedPaymentOption = paymentOptionRepository.save(paymentOption);

        // Assert
        assertThat(savedPaymentOption).isNotNull();
        assertThat(savedPaymentOption.getId()).isEqualTo(paymentOption.getId());
        assertThat(savedPaymentOption.getBranch()).isEqualTo(branch);
        assertThat(savedPaymentOption.getName()).isEqualTo("VAT");
        assertThat(savedPaymentOption.getDescription()).isEqualTo("Descripcion");
    }

    @Test
    void shouldGetAllPaymentOptionsByBranchId() {
        int nPaymentOptions = 10;
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < nPaymentOptions; i++) {
            utils.createPaymentOption(branch);
            utils.createPaymentOption(null);
        }

        List<PaymentOption> paymentOptions = paymentOptionRepository.findAllByBranchId(branch.getId());

        assertThat(paymentOptions.size()).isEqualTo(nPaymentOptions);
    }

    @Test
    void shouldCheckThatPaymentOptionExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        PaymentOption paymentOption = utils.createPaymentOption(branch);

        assertThat(paymentOptionRepository.existsByIdAndBranch_Business_Id(paymentOption.getId(),
                branch.getBusiness().getId())).isTrue();
    }

    @Test
    void shouldCheckThatPaymentOptionDoesNotExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        PaymentOption paymentOption = utils.createPaymentOption(branch);

        assertThat(paymentOptionRepository.existsByIdAndBranch_Business_Id(paymentOption.getId(),
                branch.getBusiness().getId() + 1)).isFalse();
    }
}

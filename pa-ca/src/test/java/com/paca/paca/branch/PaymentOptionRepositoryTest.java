package com.paca.paca.branch;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PaymentOptionRepositoryTest extends RepositoryTest {

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

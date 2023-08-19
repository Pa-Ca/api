package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.branch.utils.PaymentOptionMapperImpl;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)

public class PaymentOptionMapperTest {
    @InjectMocks
    private PaymentOptionMapperImpl paymentOptionMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMappaymentOptionEntityTopaymentOptionDTO() {
        PaymentOption paymentOption = utils.createPaymentOption(null);

        PaymentOptionDTO response = paymentOptionMapper.toDTO(paymentOption);
        PaymentOptionDTO expected = new PaymentOptionDTO(
                paymentOption.getId(),
                paymentOption.getBranch().getId(),
                paymentOption.getName(),
                paymentOption.getDescription());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMappaymentOptionDTOtopaymentOptionEntity() {
        Branch branch = utils.createBranch(null);
        PaymentOption paymentOption = utils.createPaymentOption(branch);
        PaymentOptionDTO paymentOptionDTO = utils.createPaymentOptionDTO(paymentOption);

        PaymentOption response = paymentOptionMapper.toEntity(paymentOptionDTO, branch);
        PaymentOption expected = new PaymentOption(
                paymentOptionDTO.getId(),
                branch,
                paymentOptionDTO.getName(),
                paymentOptionDTO.getDescription());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMappaymentOptionDTOtopaymentOptionEntity() {
        PaymentOption paymentOption = utils.createPaymentOption(null);

        PaymentOptionDTO paymentOptionDTO = new PaymentOptionDTO(
                paymentOption.getId() + 1,
                paymentOption.getBranch().getId() + 1,
                paymentOption.getName() + ".",
                paymentOption.getDescription() + ".");
        PaymentOption response = paymentOptionMapper.toEntity(paymentOptionDTO, paymentOption.getBranch());
        PaymentOption expected = new PaymentOption(
                paymentOption.getId(),
                paymentOption.getBranch(),
                paymentOptionDTO.getName(),
                paymentOptionDTO.getDescription());

        assertThat(response).isEqualTo(expected);
    }

}

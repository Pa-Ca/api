package com.paca.paca.branch;


import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
// Import Table Model and DTO
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.utils.PaymentOptionMapperImpl;
import com.paca.paca.branch.dto.PaymentOptionDTO;

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
    void shouldMappaymentOptionEntityTopaymentOptionDTO(){
        
        PaymentOption paymentOption = utils.createPaymentOption(null);

        PaymentOptionDTO paymentOptionDTO = paymentOptionMapper.toDTO(paymentOption);
        // Import the TableMapper
        // Check if the paymentOptionDTO is not null
        assertThat(paymentOptionDTO).isNotNull();
        // Check all the attributes of the paymentOptionDTO
        assertThat(paymentOptionDTO.getId()).isEqualTo(paymentOption.getId());
        assertThat(paymentOptionDTO.getBranchId()).isEqualTo(paymentOption.getBranch().getId());
        assertThat(paymentOptionDTO.getName()).isEqualTo(paymentOption.getName());
        assertThat(paymentOptionDTO.getDescription()).isEqualTo(paymentOption.getDescription());
    }

    @Test
    void shouldMappaymentOptionDTOtopaymentOptionEntity(){
            
        // Create a branch
        Branch branch = utils.createBranch(null);

        PaymentOptionDTO paymentOptionDTO = utils.createPaymentOptionDTO(branch);

        PaymentOption paymentOption = paymentOptionMapper.toEntity(paymentOptionDTO, branch);

        // Check if the paymentOption is not null
        assertThat(paymentOption).isNotNull();

        // Check tha all the attributes of the paymentOption are equal to the attributes of the paymentOptionDTO
        assertThat(paymentOption.getId()).isEqualTo(paymentOptionDTO.getId());
        assertThat(paymentOption.getName()).isEqualTo(paymentOptionDTO.getName());
        assertThat(paymentOption.getBranch().getId()).isEqualTo(paymentOptionDTO.getBranchId());
        assertThat(paymentOption.getDescription()).isEqualTo(paymentOptionDTO.getDescription());
    }

    @Test
    void shouldPartiallyMappaymentOptionDTOtopaymentOptionEntity(){

        // Create a branch
        Branch branch = utils.createBranch(null);

        // Create a paymentOptionDTO
        PaymentOptionDTO paymentOptionDTO = utils.createPaymentOptionDTO(branch);

        // Create a paymentOption from the paymentOptionDTO
        PaymentOption paymentOption = paymentOptionMapper.toEntity(paymentOptionDTO, branch);

        // Modify the paymentOptionDTO with new values
        paymentOptionDTO.setName("newName");
        paymentOptionDTO.setDescription("Metodo de pago especialmente especial");
        paymentOptionDTO.setBranchId(branch.getId() + 1);
        paymentOptionDTO.setId(paymentOption.getId() + 1);

        // Update the paymentOption with the new values from the paymentOptionDTO
        paymentOptionMapper.updateModel(paymentOptionDTO, paymentOption);

        // Chceck that the paymentOption has the new values
        assertThat(paymentOption.getName()).isEqualTo(paymentOptionDTO.getName());
        assertThat(paymentOption.getDescription()).isEqualTo(paymentOptionDTO.getDescription());

        // Check that the paymentOption has the old values for the protected attributes
        assertThat(paymentOption.getBranch().getId()).isNotEqualTo(paymentOptionDTO.getBranchId());
        assertThat(paymentOption.getId()).isNotEqualTo(paymentOptionDTO.getId());


    }

}

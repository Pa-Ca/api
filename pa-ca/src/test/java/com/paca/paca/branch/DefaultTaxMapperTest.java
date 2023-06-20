package com.paca.paca.branch;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
// Import Table Model and DTO
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.statics.DefaultTaxStatics;
import com.paca.paca.branch.utils.DefaultTaxMapperImpl;
import com.paca.paca.branch.dto.DefaultTaxDTO;
// Import the TableMapper



import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)

public class DefaultTaxMapperTest {
    @InjectMocks
    private DefaultTaxMapperImpl defaultTaxMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapDefaultTaxEntityToDefaultTaxDTO(){
        
        DefaultTax defaultTax = utils.createDefaultTax(null);

        DefaultTaxDTO defaultTaxDTO = defaultTaxMapper.toDTO(defaultTax);

        // Check if the defaultTaxDTO is not null
        assertThat(defaultTaxDTO).isNotNull();
        // Check all the attributes of the defaultTaxDTO
        assertThat(defaultTaxDTO.getId()).isEqualTo(defaultTax.getId());
        assertThat(defaultTaxDTO.getBranchId()).isEqualTo(defaultTax.getBranch().getId());
        assertThat(defaultTaxDTO.getName()).isEqualTo(defaultTax.getName());
        assertThat(defaultTaxDTO.getValue()).isEqualTo(defaultTax.getValue());
    }

    @Test
    void shouldMapDefaultTaxDTOtoDefaultTaxEntity(){
            
        // Create a branch
        Branch branch = utils.createBranch(null);

        DefaultTaxDTO defaultTaxDTO = utils.createDefaultTaxDTO(branch);

        DefaultTax defaultTax = defaultTaxMapper.toEntity(defaultTaxDTO, branch);

        // Check if the defaultTax is not null
        assertThat(defaultTax).isNotNull();

        // Check tha all the attributes of the defaultTax are equal to the attributes of the defaultTaxDTO
        assertThat(defaultTax.getId()).isEqualTo(defaultTaxDTO.getId());
        assertThat(defaultTax.getName()).isEqualTo(defaultTaxDTO.getName());
        assertThat(defaultTax.getBranch().getId()).isEqualTo(defaultTaxDTO.getBranchId());
        assertThat(defaultTax.getValue()).isEqualTo(defaultTaxDTO.getValue());
        assertThat(defaultTax.getType()).isEqualTo(defaultTaxDTO.getType());
    }

    @Test
    void shouldPartiallyMapDefaultTaxDTOtoDefaultTaxEntity(){

        // Create a branch
        Branch branch = utils.createBranch(null);

        // Create a defaultTaxDTO
        DefaultTaxDTO defaultTaxDTO = utils.createDefaultTaxDTO(branch);

        // Create a defaultTax from the defaultTaxDTO
        DefaultTax defaultTax = defaultTaxMapper.toEntity(defaultTaxDTO, branch);

        // Modify the defaultTaxDTO with new values
        defaultTaxDTO.setName("newName");
        defaultTaxDTO.setValue(0.5f);
        defaultTaxDTO.setType(DefaultTaxStatics.Types.FIXED);
        defaultTaxDTO.setBranchId(branch.getId() + 1);
        defaultTaxDTO.setId(defaultTax.getId() + 1);

        // Update the defaultTax with the new values from the defaultTaxDTO
        defaultTaxMapper.updateModel(defaultTaxDTO, defaultTax);

        // Chceck that the defaultTax has the new values
        assertThat(defaultTax.getName()).isEqualTo(defaultTaxDTO.getName());
        assertThat(defaultTax.getValue()).isEqualTo(defaultTaxDTO.getValue());
        assertThat(defaultTax.getType()).isEqualTo(defaultTaxDTO.getType());

        // Check that the defaultTax has the old values for the protected attributes
        assertThat(defaultTax.getBranch().getId()).isNotEqualTo(defaultTaxDTO.getBranchId());
        assertThat(defaultTax.getId()).isNotEqualTo(defaultTaxDTO.getId());


    }

}

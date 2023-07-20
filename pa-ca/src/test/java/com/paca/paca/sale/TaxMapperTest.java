package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.model.Sale;
// Import Table Model and DTO
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.statics.TaxStatics;
import com.paca.paca.sale.utils.TaxMapperImpl;
import com.paca.paca.sale.dto.TaxDTO;
// Import the TableMapper



import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(SpringExtension.class)

public class TaxMapperTest {
    @InjectMocks
    private TaxMapperImpl TaxMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapTaxEntityToTaxDTO(){
        
        Tax Tax = utils.createTax(null);

        TaxDTO TaxDTO = TaxMapper.toDTO(Tax);

        // Check if the TaxDTO is not null
        assertThat(TaxDTO).isNotNull();
        // Check all the attributes of the TaxDTO
        assertThat(TaxDTO.getId()).isEqualTo(Tax.getId());
        assertThat(TaxDTO.getSaleId()).isEqualTo(Tax.getSale().getId());
        assertThat(TaxDTO.getName()).isEqualTo(Tax.getName());
        assertThat(TaxDTO.getValue()).isEqualTo(Tax.getValue());
    }

    @Test
    void shouldMapTaxDTOtoTaxEntity(){
            
        // Create a sale
        Sale sale = utils.createSale(null, null, null);

        TaxDTO TaxDTO = utils.createTaxDTO(sale);

        Tax Tax = TaxMapper.toEntity(TaxDTO, sale);

        // Check if the Tax is not null
        assertThat(Tax).isNotNull();

        // Check tha all the attributes of the Tax are equal to the attributes of the TaxDTO
        assertThat(Tax.getId()).isEqualTo(TaxDTO.getId());
        assertThat(Tax.getName()).isEqualTo(TaxDTO.getName());
        assertThat(Tax.getSale().getId()).isEqualTo(TaxDTO.getSaleId());
        assertThat(Tax.getValue()).isEqualTo(TaxDTO.getValue());
        assertThat(Tax.getType()).isEqualTo(TaxDTO.getType());
    }

    @Test
    void shouldPartiallyMapTaxDTOtoTaxEntity(){

        // Create a sale
        Sale sale = utils.createSale(null, null, null);

        // Create a TaxDTO
        TaxDTO TaxDTO = utils.createTaxDTO(sale);

        // Create a Tax from the TaxDTO
        Tax Tax = TaxMapper.toEntity(TaxDTO, sale);

        // Modify the TaxDTO with new values
        TaxDTO.setName("newName");
        TaxDTO.setValue(0.5f);
        TaxDTO.setType(TaxStatics.Types.FIXED);
        TaxDTO.setSaleId(sale.getId() + 1);
        TaxDTO.setId(Tax.getId() + 1);

        // Update the Tax with the new values from the TaxDTO
        TaxMapper.updateModel(TaxDTO, Tax);

        // Chceck that the Tax has the new values
        assertThat(Tax.getName()).isEqualTo(TaxDTO.getName());
        assertThat(Tax.getValue()).isEqualTo(TaxDTO.getValue());
        assertThat(Tax.getType()).isEqualTo(TaxDTO.getType());

        // Check that the Tax has the old values for the protected attributes
        assertThat(Tax.getSale().getId()).isNotEqualTo(TaxDTO.getSaleId());
        assertThat(Tax.getId()).isNotEqualTo(TaxDTO.getId());


    }

}

package com.paca.paca.sale;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.utils.TaxMapperImpl;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class TaxMapperTest {

    @InjectMocks
    private TaxMapperImpl taxMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapTaxEntityToTaxDTO() {
        Tax Tax = utils.createTax();

        TaxDTO response = taxMapper.toDTO(Tax);
        TaxDTO expected = new TaxDTO(
                Tax.getId(),
                Tax.getType(),
                Tax.getName(),
                Tax.getValue());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapTaxDTOtoTaxEntity() {
        Tax tax = utils.createTax();
        TaxDTO taxDTO = utils.createTaxDTO(tax);

        Tax response = taxMapper.toEntity(taxDTO);
        Tax expected = new Tax(
                tax.getId(),
                tax.getType(),
                tax.getName(),
                tax.getValue());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapTaxDTOtoTaxEntity() {
        Tax tax = utils.createTax();

        TaxDTO taxDTO = new TaxDTO(
                tax.getId() + 1,
                (short) (tax.getType() + 1),
                tax.getName() + ".",
                tax.getValue() + 1);
        Tax response = taxMapper.toEntity(taxDTO);
        Tax expected = new Tax(
                tax.getId(),
                taxDTO.getType(),
                taxDTO.getName(),
                taxDTO.getValue());

        assertThat(response).isEqualTo(expected);
    }

}

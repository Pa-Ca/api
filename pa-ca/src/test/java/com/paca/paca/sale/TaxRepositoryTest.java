package com.paca.paca.sale;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.DefaultTax;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TaxRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllTaxesByBranchId() {
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

        List<Tax> taxes = taxRepository.findAllByBranchId(branch.getId());

        assertThat(taxes.size()).isEqualTo(nTaxes);
    }

}

package com.paca.paca.branch;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.DefaultTax;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DefaultTaxRepositoryTest extends RepositoryTest {

    @Test
    void shouldCheckThatDefaultTaxExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        Tax tax = utils.createTax();
        DefaultTax defaultTax = defaultTaxRepository.save(DefaultTax.builder()
                .branch(branch)
                .tax(tax)
                .build());

        Boolean exists = defaultTaxRepository.existsByIdAndBranch_Business_Id(defaultTax.getId(),
                branch.getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatDefaultTaxDoesNotExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        Tax tax = utils.createTax();
        DefaultTax defaultTax = defaultTaxRepository.save(DefaultTax.builder()
                .branch(branch)
                .tax(tax)
                .build());

        Boolean exists = defaultTaxRepository.existsByIdAndBranch_Business_Id(defaultTax.getId(),
                branch.getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }

}

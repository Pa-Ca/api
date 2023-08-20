package com.paca.paca.sale;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.sale.model.InsiteSale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InsiteSaleRepositoryTest extends RepositoryTest {

    @Test
    void shouldFindBySaleId() {
        InsiteSale insiteSale = utils.createInsiteSale(null, null);

        Optional<InsiteSale> response = insiteSaleRepository.findBySaleId(insiteSale.getSale().getId());

        assertThat(response.isPresent()).isTrue();
    }

    @Test
    void shouldNotFindBySaleId() {
        InsiteSale insiteSale = utils.createInsiteSale(null, null);

        Optional<InsiteSale> response = insiteSaleRepository.findBySaleId(insiteSale.getSale().getId() + 1);

        assertThat(response.isPresent()).isFalse();
    }
}

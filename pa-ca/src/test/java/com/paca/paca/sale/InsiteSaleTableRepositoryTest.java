package com.paca.paca.sale;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.sale.model.InsiteSale;
import com.paca.paca.sale.model.InsiteSaleTable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InsiteSaleTableRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllByInsiteSaleId() {
        int nTables = 10;
        InsiteSale sale = utils.createInsiteSale(null, null);

        for (int i = 0; i < nTables; i++) {
            utils.createInsiteSaleTable(sale, null);
            utils.createInsiteSaleTable(null, null);
        }

        List<InsiteSaleTable> tables = insiteSaleTableRepository.findAllByInsiteSaleId(sale.getId());

        assertThat(tables.size()).isEqualTo(nTables);
    }
}

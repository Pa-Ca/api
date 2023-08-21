package com.paca.paca.sale;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleTax;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SaleTaxRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllBySaleId() {
        int nTaxes = 10;
        Sale sale = utils.createSale(null, null, null);

        for (int i = 0; i < nTaxes; i++) {
            utils.createSaleTax(sale, null);
            utils.createSaleTax(null, null);
        }

        List<SaleTax> taxes = saleTaxRepository.findAllBySaleId(sale.getId());

        assertThat(taxes.size()).isEqualTo(nTaxes);
    }

    @Test
    void shouldDeleteAllBySaleId() {
        int nTaxes = 10;
        Sale sale = utils.createSale(null, null, null);

        for (int i = 0; i < nTaxes; i++) {
            utils.createSaleTax(sale, null);
            utils.createSaleTax(null, null);
        }

        saleTaxRepository.deleteAllBySaleId(sale.getId());

        List<SaleTax> taxes = saleTaxRepository.findAllBySaleId(sale.getId());

        assertThat(taxes.size()).isEqualTo(0);
    }

    @Test
    void shouldCheckThatExistsByTaxIdAndSale_Branch_Business_Id() {
        Sale sale = utils.createSale(null, null, null);
        SaleTax tax = utils.createSaleTax(sale, null);

        Boolean exists = saleTaxRepository.existsByTaxIdAndSale_Branch_Business_Id(
                tax.getTax().getId(),
                sale.getBranch().getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatDoesNotExistsByTaxIdAndSale_Branch_Business_Id() {
        Sale sale = utils.createSale(null, null, null);
        SaleTax tax = utils.createSaleTax(sale, null);

        Boolean exists = saleTaxRepository.existsByTaxIdAndSale_Branch_Business_Id(
                tax.getId(),
                sale.getBranch().getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }
}

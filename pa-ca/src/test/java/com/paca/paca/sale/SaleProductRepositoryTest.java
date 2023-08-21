package com.paca.paca.sale;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.model.SaleProduct;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SaleProductRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllBySaleId() {
        int nProducts = 10;
        Sale sale = utils.createSale(null, null, null);

        for (int i = 0; i < nProducts; i++) {
            utils.createSaleProduct(sale, null);
            utils.createSaleProduct(null, null);
        }

        List<SaleProduct> products = saleProductRepository.findAllBySaleId(sale.getId());

        assertThat(products.size()).isEqualTo(nProducts);
    }

    @Test
    void shouldDeleteAllbySaleId() {
        int nProducts = 10;
        Sale sale = utils.createSale(null, null, null);

        for (int i = 0; i < nProducts; i++) {
            utils.createSaleProduct(sale, null);
            utils.createSaleProduct(null, null);
        }

        saleProductRepository.deleteAllBySaleId(sale.getId());

        List<SaleProduct> products = saleProductRepository.findAllBySaleId(sale.getId());

        assertThat(products.size()).isEqualTo(0);
    }

    @Test
    void shoudCheckThatExistsByIdAndSale_Table_Branch_Business_Id() {
        Sale sale = utils.createSale(null, null, null);
        Product product = utils.createProduct(null);
        SaleProduct saleProduct = utils.createSaleProduct(sale, product);

        Boolean exists = saleProductRepository.existsByIdAndSale_Branch_Business_Id(
                saleProduct.getId(),
                sale.getBranch().getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shoudCheckThatDoesNotExistsByIdAndSale_Table_Branch_Business_Id() {
        Sale sale = utils.createSale(null, null, null);
        Product product = utils.createProduct(null);
        SaleProduct saleProduct = utils.createSaleProduct(sale, product);

        Boolean exists = saleProductRepository.existsByIdAndSale_Branch_Business_Id(
                saleProduct.getId(),
                sale.getBranch().getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }

}

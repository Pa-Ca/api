package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.utils.SaleProductMapperImpl;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
public class SaleProductMapperTest {

    @InjectMocks
    private SaleProductMapperImpl saleProductMapper;

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapSaleProductEntityToSaleProductDTO() {
        SaleProduct saleProduct = utils.createSaleProduct(null, null);

        SaleProductDTO response = saleProductMapper.toDTO(saleProduct);
        SaleProductDTO expected = new SaleProductDTO(
                saleProduct.getId(),
                saleProduct.getSale().getId(),
                saleProduct.getProduct().getId(),
                saleProduct.getName(),
                saleProduct.getAmount(),
                saleProduct.getPrice());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapSaleProductDTOtoSaleProductEntity() {
        SaleProduct saleProduct = utils.createSaleProduct(null, null);
        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(saleProduct);

        SaleProduct response = saleProductMapper.toEntity(
                saleProductDTO,
                saleProduct.getSale(),
                saleProduct.getProduct());
        SaleProduct expected = new SaleProduct(
                saleProduct.getId(),
                saleProduct.getSale(),
                saleProduct.getProduct(),
                saleProduct.getName(),
                saleProduct.getAmount(),
                saleProduct.getPrice());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapSaleProductDTOtoSaleProductEntity() {
        SaleProduct saleProduct = utils.createSaleProduct(null, null);

        SaleProductDTO saleProductDTO = new SaleProductDTO(
                saleProduct.getId() + 1,
                saleProduct.getSale().getId() + 1,
                saleProduct.getProduct().getId() + 1,
                saleProduct.getName() + "1",
                saleProduct.getAmount() + 1,
                saleProduct.getPrice().add(BigDecimal.ONE));
        SaleProduct response = saleProductMapper.updateModel(
                saleProductDTO,
                saleProduct);
        SaleProduct expected = new SaleProduct(
                saleProduct.getId(),
                saleProduct.getSale(),
                saleProduct.getProduct(),
                saleProductDTO.getName(),
                saleProductDTO.getAmount(),
                saleProduct.getPrice());

        assertThat(response).isEqualTo(expected);
    }
}

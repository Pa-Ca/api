package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
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
    void shouldMapSaleProductEntityToSaleProductDTO(){

        SaleProduct saleProduct = utils.createSaleProduct(null, null);

        SaleProductDTO salePorductDTO = saleProductMapper.toDTO(saleProduct);

        // Check if the saleProductDTO is not null
        assertThat(salePorductDTO).isNotNull();
        // Check all the attributes of the salePoructDTO
        assertThat(salePorductDTO.getId()).isEqualTo(saleProduct.getId());
        assertThat(salePorductDTO.getAmmount()).isEqualTo(saleProduct.getAmmount());
        assertThat(salePorductDTO.getSaleId()).isEqualTo(saleProduct.getSale().getId());
        assertThat(salePorductDTO.getProductId()).isEqualTo(saleProduct.getProduct().getId());
        assertThat(salePorductDTO.getName()).isEqualTo(saleProduct.getName());
        assertThat(salePorductDTO.getPrice()).isEqualTo(saleProduct.getPrice());

    }

    @Test
    void shouldMapSaleProductDTOtoSaleProductEntity(){

        // Create a sale and a product 
        Sale sale = utils.createSale(null, null);
        Product product = utils.createProduct(null);

        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(sale, product);

        SaleProduct saleProduct = saleProductMapper.toEntity(saleProductDTO, sale, product);

        // Check if the saleProduct is not null
        assertThat(saleProduct).isNotNull();
        // Check all the attributes of the saleProduct
        assertThat(saleProduct.getId()).isEqualTo(saleProductDTO.getId());
        assertThat(saleProduct.getAmmount()).isEqualTo(saleProductDTO.getAmmount());
        assertThat(saleProduct.getSale().getId()).isEqualTo(saleProductDTO.getSaleId());
        assertThat(saleProduct.getProduct().getId()).isEqualTo(saleProductDTO.getProductId());
        assertThat(saleProduct.getName()).isEqualTo(saleProductDTO.getName());
        assertThat(saleProduct.getPrice()).isEqualTo(saleProductDTO.getPrice());

    }

    @Test
    void shouldPartiallyMapSaleProductDTOtoSaleProductEntity(){
        // This function should check that when an enetity is updated by a dto, all the fileds are
        // updated except for id, product, sale and price


        // Create a sale and a product 
        Sale sale = utils.createSale(null, null);
        Product product = utils.createProduct(null);

        SaleProductDTO saleProductDTO = utils.createSaleProductDTO(sale, product);

        SaleProduct saleProduct = saleProductMapper.toEntity(saleProductDTO, sale, product);

        // Check if the saleProduct is not null
        assertThat(saleProduct).isNotNull();
        // Check all the attributes of the saleProduct
        assertThat(saleProduct.getId()).isEqualTo(saleProductDTO.getId());
        assertThat(saleProduct.getAmmount()).isEqualTo(saleProductDTO.getAmmount());
        assertThat(saleProduct.getSale().getId()).isEqualTo(saleProductDTO.getSaleId());
        assertThat(saleProduct.getProduct().getId()).isEqualTo(saleProductDTO.getProductId());
        assertThat(saleProduct.getName()).isEqualTo(saleProductDTO.getName());
        assertThat(saleProduct.getPrice()).isEqualTo(saleProductDTO.getPrice());

        // Update all of the fileds in the saleProductDTO
        // Generate a random long for the id
        long random_id = (long) (Math.random() * 1000);
        BigDecimal random_price = BigDecimal.valueOf(Math.random() * 1000);
        saleProductDTO.setId(random_id);
        saleProductDTO.setAmmount(10);
        saleProductDTO.setName("New name");
        saleProductDTO.setPrice(random_price);
        saleProductDTO.setProductId(random_id+1);
        saleProductDTO.setSaleId(random_id+2);

        // Update the saleProduct entity
        saleProduct = saleProductMapper.updateModel(saleProductDTO, saleProduct);

        // Now check that the saleProduct entity has been updated
        assertThat(saleProduct).isNotNull();
        // Check all the non updatable attributes of the saleProduct
        assertThat(saleProduct.getId()).isNotEqualTo(saleProductDTO.getId());
        assertThat(saleProduct.getSale().getId()).isNotEqualTo(saleProductDTO.getSaleId());
        assertThat(saleProduct.getProduct().getId()).isNotEqualTo(saleProductDTO.getProductId());
        assertThat(saleProduct.getPrice()).isNotEqualTo(saleProductDTO.getPrice());
        
        // Check that only the updatable fields have been updated
        assertThat(saleProduct.getAmmount()).isEqualTo(saleProductDTO.getAmmount());
        assertThat(saleProduct.getName()).isEqualTo(saleProductDTO.getName());
        


    }
}

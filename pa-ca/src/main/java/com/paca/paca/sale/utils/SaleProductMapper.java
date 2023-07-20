package com.paca.paca.sale.utils;

import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.dto.SaleProductDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleProductMapper {

    @Mapping(source = "sale.id", target = "saleId")
    @Mapping(source = "product.id", target = "productId")
    SaleProductDTO toDTO(SaleProduct saleProduct);

    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "product", ignore = true)
    SaleProduct toEntity(SaleProductDTO dto);

    default SaleProduct toEntity(SaleProductDTO dto, Sale sale, Product product) {
        SaleProduct saleProduct = toEntity(dto);
        saleProduct.setSale(sale);
        saleProduct.setProduct(product);

        return saleProduct;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SaleProduct updateModel(SaleProductDTO dto, @MappingTarget SaleProduct saleProduct);


}
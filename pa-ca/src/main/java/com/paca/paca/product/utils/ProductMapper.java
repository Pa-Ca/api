package com.paca.paca.product.utils;

import com.paca.paca.product.model.Product;
import com.paca.paca.product.dto.ProductDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "subCategory.id", target = "subCategoryId")
    ProductDTO toDTO(Product product);

    Product toEntity(ProductDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subCategory", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product updateModel(ProductDTO dto, @MappingTarget Product product);
}
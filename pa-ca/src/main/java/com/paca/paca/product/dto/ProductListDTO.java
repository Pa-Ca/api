package com.paca.paca.product.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDTO {

    List<ProductDTO> products;
}

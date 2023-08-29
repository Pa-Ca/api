package com.paca.paca.sale.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TaxListDTO {

    List<TaxDTO> taxes;
}

package com.paca.paca.business.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BusinessListDTO {

    List<BusinessDTO> business;
}

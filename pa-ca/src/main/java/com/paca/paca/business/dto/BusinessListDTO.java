package com.paca.paca.business.dto;

import com.paca.paca.client.dto.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessListDTO {
    List<BusinessDTO> business;
}

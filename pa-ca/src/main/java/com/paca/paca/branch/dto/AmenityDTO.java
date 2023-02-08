package com.paca.paca.branch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmenityDTO {
    private Long id;
    private String name;

    public AmenityDTO(Long id) { this.id = id; }
}

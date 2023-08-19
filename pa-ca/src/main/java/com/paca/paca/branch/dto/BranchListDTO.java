package com.paca.paca.branch.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BranchListDTO {

    List<BranchDTO> branches;
}

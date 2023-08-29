package com.paca.paca.client.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListDTO {

    List<ReviewDTO> reviews;
}

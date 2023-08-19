package com.paca.paca.client.dto;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;
    private Long clientId;
    private Long branchId;
    private String text;
    private Date date;
    private Integer likes;
}

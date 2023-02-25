package com.paca.paca.branch.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewDTO {

    private Long id;
    private Long clientId;
    private Long branchId;
    private String text;
    private Date date;
    private Integer likes;
}


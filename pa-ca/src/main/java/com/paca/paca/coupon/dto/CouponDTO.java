package com.paca.paca.coupon.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private Long id;
    private Short type;
    private Float value;
    private String description;
    private Date startDate;
    private Date endDate;
    private List<CouponItemDTO> items;
}

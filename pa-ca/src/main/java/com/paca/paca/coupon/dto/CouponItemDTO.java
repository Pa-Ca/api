package com.paca.paca.coupon.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CouponItemDTO {
    private Long id;
    private String type;
    private String name;
}

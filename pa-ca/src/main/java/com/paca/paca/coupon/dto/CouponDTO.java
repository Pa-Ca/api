package com.paca.paca.coupon.dto;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "The id field must be not null")
    private Long id;
    @NotNull(message = "The type field must be not null")
    private Short type;
    @NotNull(message = "The discountType field must be not null")
    private Short discountType;
    @NotNull(message = "The value field must be not null")
    private Float value;
    @NotNull(message = "The description field must be not null")
    private String description;
    @NotNull(message = "The startDate field must be not null")
    private Date startDate;
    @NotNull(message = "The endDate field must be not null")
    private Date endDate;
    @NotNull(message = "The items field must be not null")
    private List<CouponItemDTO> items;
}

package com.paca.paca.coupon.statics;

public interface CouponStatics {

    interface Endpoint {
        String PATH = "/api/v1/coupon";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
    }
}

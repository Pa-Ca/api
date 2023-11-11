package com.paca.paca.coupon.statics;

public interface CouponStatics {

    interface Endpoint {
        String PATH = "/api/v1/coupon";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
        String GET_BY_PAGE = "/page";
    }

    interface Type {
        int PRODUCT = 0;
        int CATEGORY = 1;
        int SUB_CATEGORY = 2;
    }
}

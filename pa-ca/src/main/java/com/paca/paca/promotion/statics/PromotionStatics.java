package com.paca.paca.promotion.statics;

public interface PromotionStatics {

    interface Endpoint {
        String PATH = "/api/v1/promotion";

        String SAVE = "";
        String GET_BY_ID = "/{id}";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
    }
}

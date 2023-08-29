package com.paca.paca.product.statics;

public interface ProductStatics {

    interface Endpoint {
        String PATH = "/api/v1/product";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
    }
}

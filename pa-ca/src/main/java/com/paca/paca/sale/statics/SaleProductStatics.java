package com.paca.paca.sale.statics;

public interface SaleProductStatics {
    interface Endpoint {
        String PATH = "/api/v1/sale-product";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
    }
}

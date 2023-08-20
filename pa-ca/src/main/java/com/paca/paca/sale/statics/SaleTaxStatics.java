package com.paca.paca.sale.statics;

public interface SaleTaxStatics {
    interface Endpoint {
        String PATH = "/api/v1/sale-tax";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
    }
}

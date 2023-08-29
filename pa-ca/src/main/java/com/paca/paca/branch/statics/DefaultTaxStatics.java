package com.paca.paca.branch.statics;

public interface DefaultTaxStatics {
    interface Endpoint {
        String PATH = "/api/v1/default-tax";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
    }
}

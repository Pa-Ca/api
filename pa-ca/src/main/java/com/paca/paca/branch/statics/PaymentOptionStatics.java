package com.paca.paca.branch.statics;

public interface PaymentOptionStatics {

    interface Endpoint {
        String PATH = "/api/v1/payment-option";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
    }
}

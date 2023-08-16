package com.paca.paca.reservation.statics;

public interface GuestStatics {

    interface Endpoint {
        String PATH = "/api/v1/guest";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
        String GET_BY_IDENTITY_DOCUMENT = "/identity-document/{identityDocument}";
    }
}
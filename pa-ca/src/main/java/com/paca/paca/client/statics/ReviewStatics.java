package com.paca.paca.client.statics;

public interface ReviewStatics {

    interface Endpoint {
        String PATH = "/api/v1/review";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
        String LIKE = "/{id}/client/{clientId}";
        String DISLIKE = "/{id}/client/{clientId}";
    }
}

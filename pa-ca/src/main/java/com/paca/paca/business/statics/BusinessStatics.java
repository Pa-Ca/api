package com.paca.paca.business.statics;

public interface BusinessStatics {
    interface Endpoint {
        String PATH = "/api/v1/business";

        String SAVE = "";
        String GET_ALL = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
        String GET_BY_USER_ID = "/user/{id}";
    }
}
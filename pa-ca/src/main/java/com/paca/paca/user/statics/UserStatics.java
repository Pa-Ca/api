package com.paca.paca.user.statics;

public interface UserStatics {

    interface Endpoint {
        String PATH = "/api/v1/user";

        String GET_ALL = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
    }
}

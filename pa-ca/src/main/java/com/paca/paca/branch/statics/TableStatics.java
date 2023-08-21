package com.paca.paca.branch.statics;

public interface TableStatics {

    interface Endpoint {
        String PATH = "/api/v1/table";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
    }

}

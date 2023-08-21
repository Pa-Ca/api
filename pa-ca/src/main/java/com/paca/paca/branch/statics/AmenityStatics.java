package com.paca.paca.branch.statics;

public interface AmenityStatics {

    interface Endpoint {
        String PATH = "/api/v1/amenity";

        String GET_ALL = "";
        String SEARCH = "/search/{word}";
        String GET_BRANCHES = "/{id}/branch";
    }
}

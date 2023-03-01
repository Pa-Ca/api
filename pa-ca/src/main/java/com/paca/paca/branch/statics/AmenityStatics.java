package com.paca.paca.branch.statics;

public interface AmenityStatics {

    interface Endpoint {
        String AMENITY_PATH = "/amenity";
        String AMENITY_BRANCHES = "/{id}/branch";
        String AMENITY_SEARCH_PATH = "/search/{word}";
    }
}

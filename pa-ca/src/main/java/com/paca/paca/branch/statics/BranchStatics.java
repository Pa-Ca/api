package com.paca.paca.branch.statics;

public interface BranchStatics {

    interface Endpoint {
        String PATH = "/api/v1/branch";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";

        String SAVE_AMENITIES = "/{id}/amenity";
        String DELETE_AMENITIES = "/{id}/amenity/{amenityId}";

        String GET_SALES = "/{id}/sale";
        String GET_REVIEWS = "/{id}/review";
        String GET_ALL_TABLES = "/{id}/table";
        String GET_ALL_PRODUCTS = "/{id}/product";
        String GET_ALL_AMENITIES = "/{id}/amenity";
        String GET_RESERVATIONS = "/{id}/reservation";
        String GET_ALL_PROMOTIONS = "/{id}/promotion";
        String GET_ALL_PAYMENT_OPTIONS = "/{id}/payment-option";
        String GET_ALL_FAVORITE_CLIENTS = "/{id}/favorite-client";
        String GET_ALL_PRODUCT_SUB_CATEGORIES = "/{id}/product-sub-category";
    }
}

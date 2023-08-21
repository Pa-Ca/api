package com.paca.paca.productSubCategory.statics;

public interface ProductSubCategoryStatics {

    interface Endpoint {
        String PATH = "/api/v1/product-sub-category";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
        String GET_ALL_CATEGORIES = "/categories";
        String GET_ALL_PRODUCTS_BY_ID = "/{id}/products";
    }
}

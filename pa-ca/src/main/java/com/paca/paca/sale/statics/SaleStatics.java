package com.paca.paca.sale.statics;

import java.util.List;
import java.util.Arrays;

public interface SaleStatics {

    interface Endpoint {
        String PATH = "/api/v1/sale";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String ADD_TAX = "/{id}/tax";
        String CLEAR = "/{id}/clear";
    }

    interface Status {
        Short CANCELLED = 0;
        Short ONGOING = 1;
        Short CLOSED = 2;

        List<Short> ALL = Arrays.asList(CANCELLED, ONGOING, CLOSED);
    }
}

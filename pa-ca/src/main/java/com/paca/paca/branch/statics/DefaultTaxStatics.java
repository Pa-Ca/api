package com.paca.paca.branch.statics;

public interface DefaultTaxStatics {
    interface Endpoint {
        String PATH = "/api/v1/default-tax";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
    }

    interface Types {
        Short FIXED = 0;
        Short PERCENTAGE = 1;

        static boolean isTypeValid(Short type) {
            return type.equals(FIXED) || type.equals(PERCENTAGE);
        }
    }

}

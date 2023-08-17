package com.paca.paca.sale.statics;

public interface TaxStatics {

    interface Endpoint {
        String PATH = "/api/v1/tax";

        String SAVE = "";
    }

    interface Types {
        Short FIXED = 0;
        Short PERCENTAGE = 1;

        static boolean isTypeValid(Short type) {
            return type.equals(FIXED) || type.equals(PERCENTAGE);
        }
    }

}

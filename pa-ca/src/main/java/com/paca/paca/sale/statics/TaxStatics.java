package com.paca.paca.sale.statics;

public interface TaxStatics {

    interface Endpoint {
        String PATH = "/api/v1/tax";

        String SAVE = "";
    }


    interface Types {
        Integer FIXED = 0;
        Integer PERCENTAGE = 1; 
        
        static boolean isTypeValid(Integer type) {
            return type.equals(FIXED) || type.equals(PERCENTAGE);
        }
    }

    
}

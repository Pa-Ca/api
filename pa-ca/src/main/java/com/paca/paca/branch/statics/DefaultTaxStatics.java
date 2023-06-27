package com.paca.paca.branch.statics;

public interface DefaultTaxStatics {
    interface Endpoint {
        String PATH = "/api/v1/defaulttax";

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

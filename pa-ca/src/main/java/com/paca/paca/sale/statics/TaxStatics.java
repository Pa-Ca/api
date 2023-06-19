package com.paca.paca.sale.statics;

public interface TaxStatics {
    interface Types {
        Integer FIXED = 0;
        Integer PERCENTAGE = 1; 
        
        static boolean isTypeValid(Integer type) {
            return type.equals(FIXED) || type.equals(PERCENTAGE);
        }
    }

    
}

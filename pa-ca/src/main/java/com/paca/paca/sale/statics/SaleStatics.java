package com.paca.paca.sale.statics;

public interface SaleStatics {

    interface Endpoint {
        String PATH = "/api/v1/sale";

        String SAVE = "";
    }

    interface SaleSortingKeys {
        String ENDTIME = "end_time";
        String STARTTIME = "start_time";

        static boolean contains(String sorting_key) {
            return sorting_key.equals(ENDTIME) ||
                    sorting_key.equals(STARTTIME);
        }
    }

    interface Status {
        Integer canceled = 0;
        Integer ongoing = 1;
        Integer closed  = 2;    
    }
}


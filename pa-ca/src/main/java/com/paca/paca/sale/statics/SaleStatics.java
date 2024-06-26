package com.paca.paca.sale.statics;

public interface SaleStatics {

    interface Endpoint {
        String PATH = "/api/v1/sale";

        String SAVE = "";
    }

    interface SaleSortingKeys {
        String ENDTIME = "endTime";
        String STARTTIME = "startTime";

        static boolean contains(String sorting_key) {
            return sorting_key.equals(ENDTIME) ||
                    sorting_key.equals(STARTTIME);
        }
    }

    interface Status {
        Integer canceled = 0;
        Integer cancelled = 0;
        Integer ongoing = 1;
        Integer closed = 2;    
    }
}


package com.paca.paca.branch.statics;

public interface BranchStatics {

    interface Endpoint {
        String PATH = "/api/v1/branch";

        String SAVE = "";
    }

    interface BranchSortingKeys {
        String SCORE = "score";
        String CAPACITY = "capacity";
        String RESERVATION_PRICE = "reservation_price";
        String NAME = "name";

        static boolean contains(String sorting_key) {
            return sorting_key.equals(SCORE) ||
                    sorting_key.equals(CAPACITY) ||
                    sorting_key.equals(RESERVATION_PRICE) ||
                    sorting_key.equals(NAME);
        }
    }
}

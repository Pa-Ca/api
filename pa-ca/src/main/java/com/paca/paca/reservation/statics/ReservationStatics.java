package com.paca.paca.reservation.statics;

public interface ReservationStatics {

    interface Endpoint {
        String PATH = "/api/v1/reservation";
        String GUEST_PATH = "/api/v1/guest";
    }

    interface Status {
        int unset = 0;
        int pending = 1;
        int accepted = 2;
        int rejected = 3;
        int closed = 4;

        // Needs an agreement
        int canceled = 90;
        int paid = 91;
        int returned = 92;
    }
}

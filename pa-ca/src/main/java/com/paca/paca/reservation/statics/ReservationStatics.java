package com.paca.paca.reservation.statics;

public interface ReservationStatics {

    interface Endpoint {
        String PATH = "/api/v1/reservation";
        String GUEST_PATH = "/api/v1/guest";
    }

    interface Status {
        int unset = 0;
        int pending = 1;
        int rejected = 2;
        int accepted = 3;
        int retired = 4;
        int started = 5;
        int closed = 6;

        // Needs an agreement
        int canceled = 90;
        int paid = 91;
        int returned = 92;
    }
}
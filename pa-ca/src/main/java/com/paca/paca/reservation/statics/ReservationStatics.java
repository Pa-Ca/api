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
        int paid = 3;
        int accepted = 4;
        int canceled = 5;
        int closed = 6;
        int returned = 7;
    }
}

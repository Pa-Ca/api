package com.paca.paca.reservation.statics;

public interface ReservationStatics {

    interface Endpoint {
        String PATH = "/api/v1/reservation";
    }

    interface Status {
        int unset = 0;
        int pending = 1;
        int rejected = 2;
        int paid = 3;
        int accepted = 4;
        int canceled = 5;
        int closed = 5;
        int returned = 6;
    }
}

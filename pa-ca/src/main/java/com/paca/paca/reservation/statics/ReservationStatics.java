package com.paca.paca.reservation.statics;

import java.util.List;

public interface ReservationStatics {

    interface Endpoint {
        String PATH = "/api/v1/reservation";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
        String CANCEL = "/{id}/cancel";
        String REJECT = "/{id}/reject";
        String ACCEPT = "/{id}/accept";
        String RETIRE = "/{id}/retire";
        String START = "/{id}/start";
        String CLOSE = "/{id}/close";
        String PAY = "/{id}/pay";
    }

    interface Status {
        Short UNSET = 0;
        Short PENDING = 1;
        Short REJECTED = 2;
        Short ACCEPTED = 3;
        Short RETIRED = 4;
        Short STARTED = 5;
        Short CLOSED = 6;

        // Needs an agreement
        Short CANCELED = 90;
        Short RETURNED = 91;

        List<Short> ALL = List.of(
                UNSET,
                PENDING,
                REJECTED,
                ACCEPTED,
                RETIRED,
                STARTED,
                CLOSED,
                CANCELED,
                RETURNED);
    }
}
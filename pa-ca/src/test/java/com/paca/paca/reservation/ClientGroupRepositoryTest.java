package com.paca.paca.reservation;

import com.paca.paca.RepositoryTest;
import com.paca.paca.client.model.Client;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClientGroupRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllByClientId() {
        Client client = utils.createClient(null);

        // Create random client groups
        for (int i = 0; i < 10; i++) {
            utils.createClientGroup(client, null);
            utils.createClientGroup(null, null);
        }

        List<ClientGroup> response = clientGroupRepository.findAllByClientId(client.getId());

        assertThat(response.size()).isEqualTo(10);
    }

    @Test
    void shouldGetAllByReservationId() {
        Reservation reservation = utils.createReservation(null);

        // Create random client groups
        for (int i = 0; i < 10; i++) {
            utils.createClientGroup(null, reservation, i == 0);
            utils.createClientGroup(null, null);
        }

        List<ClientGroup> response = clientGroupRepository.findAllByReservationId(reservation.getId());

        assertThat(response.size()).isEqualTo(10);
    }

    @Test
    void shouldFindByReservationIdAndClientId() {
        Reservation reservation = utils.createReservation(null);
        Client client = utils.createClient(null);
        utils.createClientGroup(client, reservation);

        Optional<ClientGroup> response = clientGroupRepository.findByReservationIdAndClientId(
                reservation.getId(),
                client.getId());

        assertThat(response.isPresent()).isTrue();
    }

}

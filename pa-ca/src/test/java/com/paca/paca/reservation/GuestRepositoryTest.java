package com.paca.paca.reservation;

import com.paca.paca.RepositoryTest;
import com.paca.paca.reservation.model.Guest;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GuestRepositoryTest extends RepositoryTest {

    @Test
    void shouldCheckThatGuestExistsByIdentityDocument() {
        Guest guest = utils.createGuest();

        Optional<Guest> expectedGuest = guestRepository.findByIdentityDocument(guest.getIdentityDocument());

        assertThat(expectedGuest.isPresent()).isTrue();
        assertThat(expectedGuest.get()).isEqualTo(guest);
    }

    @Test
    void shouldCheckThatGuestDoesNotExistsByIdentityDocument() {
        Optional<Guest> expectedGuest = guestRepository.findByIdentityDocument("V");

        assertThat(expectedGuest.isEmpty()).isTrue();
    }

}
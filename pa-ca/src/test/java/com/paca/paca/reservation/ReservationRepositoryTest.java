package com.paca.paca.reservation;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.statics.ReservationStatics;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReservationRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetReservationsByBranchIdAndFilters() {
        Random rand = new Random();

        // Create random branches
        List<Branch> branches = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            branches.add(utils.createBranch(null));
        }

        // Create random names
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            names.add("name_" + i + " ");
        }

        // Create random surnames
        List<String> surnames = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            surnames.add("surname_" + i + " ");
        }

        // Create random identity documents
        List<String> identityDocuments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            identityDocuments.add("V" + i);
        }

        // Min and max dates
        Date minDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minDate);
        calendar.add(Calendar.MONTH, 1);
        Date maxDate = calendar.getTime();

        List<Reservation> reservations = utils.createTestReservations(
                branches,
                minDate,
                maxDate,
                names,
                surnames,
                identityDocuments);

        // Select a random values
        Branch branch = branches.get(rand.nextInt(branches.size()));
        Date startDate = new Date(ThreadLocalRandom.current().nextLong(
                minDate.getTime(),
                maxDate.getTime()));
        Date endDate = new Date(ThreadLocalRandom.current().nextLong(
                startDate.getTime(),
                maxDate.getTime()));
        Short status = ReservationStatics.Status.ALL.get(rand.nextInt(ReservationStatics.Status.ALL.size()));
        String name = names.get(rand.nextInt(names.size()));
        String surname = surnames.get(rand.nextInt(surnames.size()));
        String identityDocument = identityDocuments.get(rand.nextInt(identityDocuments.size()));

        // Filter the reservations
        List<Reservation> expected = reservations.stream()
                .filter(reservation -> reservation.getBranch().getId().equals(branch.getId()))
                .filter(reservation -> reservation.getReservationDateIn().compareTo(startDate) >= 0)
                .filter(reservation -> reservation.getReservationDateIn().compareTo(endDate) <= 0)
                .filter(reservation -> reservation.getStatus().equals(status))
                .filter(reservation -> {
                    if (reservation.getGuest() != null) {
                        return reservation.getGuest().getName().contains(name)
                                && reservation.getGuest().getSurname().contains(surname)
                                && reservation.getGuest().getIdentityDocument().contains(identityDocument);
                    } else {
                        ClientGroup owner = clientGroupRepository.findAllByReservationId(reservation.getId()).get(0);
                        return owner.getClient().getName().contains(name)
                                && owner.getClient().getSurname().contains(surname)
                                && owner.getClient().getIdentityDocument().contains(identityDocument);
                    }
                })
                .collect(Collectors.toList());

        // Get the reservations from the repository
        List<Reservation> response = reservationRepository.findAllByBranchIdAndFilters(
                branch.getId(),
                List.of(status),
                startDate,
                endDate,
                name,
                surname,
                identityDocument);

        assertThat(response.size()).isEqualTo(expected.size());
    }

    @Test
    void shouldCheckThatReservationExistsByIdAndBranchBusinessId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByIdAndBranch_Business_Id(
                reservation.getId(),
                branch.getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatReservationDoesNotExistsByIdAndBranchBusinessId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByIdAndBranch_Business_Id(
                reservation.getId(),
                branch.getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }

    @Test
    void shouldCheckThatReservationExistsByBranchBusinessIdAndGuestId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByBranchBusinessIdAndGuestId(
                branch.getBusiness().getId(),
                reservation.getGuest().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatReservationDoesNotExistsByBranchBusinessIdAndGuestId() {
        Branch branch = utils.createBranch(null);
        Reservation reservation = utils.createReservation(branch, null);

        boolean exists = reservationRepository.existsByBranchBusinessIdAndGuestId(
                branch.getBusiness().getId() + 1,
                reservation.getGuest().getId());

        assertThat(exists).isFalse();
    }
}
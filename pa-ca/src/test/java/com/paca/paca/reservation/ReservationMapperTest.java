package com.paca.paca.reservation;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.utils.ReservationMapperImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Date;
import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
public class ReservationMapperTest {
    @InjectMocks
    private ReservationMapperImpl reservationMapper;
    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapReservationEntityToReservationDTO() {
        Reservation reservation = utils.createReservation(null, null, null);

        ReservationDTO response = reservationMapper.toDTO(reservation);
        ReservationDTO expected = new ReservationDTO(
                reservation.getId(),
                reservation.getBranch().getId(),
                reservation.getGuest().getId(),
                reservation.getInvoice().getId(),
                reservation.getRequestDate(),
                reservation.getReservationDateIn(),
                reservation.getReservationDateOut(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getTableNumber(),
                reservation.getClientNumber(),
                reservation.getOccasion(),
                reservation.getByClient());
        assertThat(response).isEqualTo(expected);

        reservation = utils.createReservation(null, null);
        response = reservationMapper.toDTO(reservation);
        expected = new ReservationDTO(
                reservation.getId(),
                reservation.getBranch().getId(),
                reservation.getGuest().getId(),
                null,
                reservation.getRequestDate(),
                reservation.getReservationDateIn(),
                reservation.getReservationDateOut(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getTableNumber(),
                reservation.getClientNumber(),
                reservation.getOccasion(),
                reservation.getByClient());
        assertThat(response).isEqualTo(expected);

        reservation = utils.createReservation(null);
        response = reservationMapper.toDTO(reservation);
        expected = new ReservationDTO(
                reservation.getId(),
                reservation.getBranch().getId(),
                null,
                null,
                reservation.getRequestDate(),
                reservation.getReservationDateIn(),
                reservation.getReservationDateOut(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getTableNumber(),
                reservation.getClientNumber(),
                reservation.getOccasion(),
                reservation.getByClient());
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldMapReservationDTOtoReservationEntity() {
        Branch branch = utils.createBranch(null);
        Guest guest = utils.createGuest();
        Invoice invoice = utils.createInvoice();
        ReservationDTO dto = utils.createReservationDTO(utils.createReservation(
                branch,
                guest,
                invoice));
        Reservation entity = reservationMapper.toEntity(dto, branch, guest, invoice);
        Reservation expected = new Reservation(
                dto.getId(),
                branch,
                guest,
                invoice,
                dto.getRequestDate(),
                dto.getReservationDateIn(),
                dto.getReservationDateOut(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getTableNumber(),
                dto.getClientNumber(),
                dto.getOccasion(),
                dto.getByClient());
        assertThat(entity).isEqualTo(expected);

        dto = utils.createReservationDTO(utils.createReservation(branch, guest));
        entity = reservationMapper.toEntity(dto, branch, guest, null);
        expected = new Reservation(
                dto.getId(),
                branch,
                guest,
                null,
                dto.getRequestDate(),
                dto.getReservationDateIn(),
                dto.getReservationDateOut(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getTableNumber(),
                dto.getClientNumber(),
                dto.getOccasion(),
                dto.getByClient());
        assertThat(entity).isEqualTo(expected);

        dto = utils.createReservationDTO(utils.createReservation(branch));
        entity = reservationMapper.toEntity(dto, branch, null, null);
        expected = new Reservation(
                dto.getId(),
                branch,
                null,
                null,
                dto.getRequestDate(),
                dto.getReservationDateIn(),
                dto.getReservationDateOut(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getTableNumber(),
                dto.getClientNumber(),
                dto.getOccasion(),
                dto.getByClient());
        assertThat(entity).isEqualTo(expected);
    }

    @Test
    void shouldPartiallyMapReservationDTOtoReservationEntity() {
        Reservation reservation = utils.createReservation(null, null, null);
        Branch branch = utils.createBranch(null);
        Guest guest = utils.createGuest();
        Invoice invoice = utils.createInvoice();

        ReservationDTO dto = new ReservationDTO(
                reservation.getId() + 1,
                branch.getId() + 1,
                guest.getId() + 1,
                invoice.getId() + 1,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()),
                new BigDecimal(100),
                ReservationStatics.Status.ACCEPTED,
                (short) (reservation.getTableNumber() + 1),
                (short) (reservation.getClientNumber() + 1),
                reservation.getOccasion() + ".",
                !reservation.getByClient());

        Reservation response = reservationMapper.updateModel(dto, reservation);
        Reservation expected = new Reservation(
                reservation.getId(),
                branch,
                guest,
                invoice,
                dto.getRequestDate(),
                dto.getReservationDateIn(),
                dto.getReservationDateOut(),
                dto.getPrice(),
                dto.getStatus(),
                dto.getTableNumber(),
                dto.getClientNumber(),
                dto.getOccasion(),
                dto.getByClient());

        assertThat(response).isEqualTo(expected);
    }
}
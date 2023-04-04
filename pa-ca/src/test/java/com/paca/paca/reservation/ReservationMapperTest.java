package com.paca.paca.reservation;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.utils.ReservationMapperImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Date;

@ExtendWith(SpringExtension.class)
public class ReservationMapperTest {
    @InjectMocks
    private ReservationMapperImpl reservationMapper;
    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldMapReservationEntityToReservationDTO() {
        Reservation reservation = utils.createReservation(null);

        ReservationDTO response = reservationMapper.toDTO(reservation);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(reservation.getId());
        assertThat(response.getBranchId()).isEqualTo(reservation.getBranch().getId());
        assertThat(response.getRequestDate()).isEqualTo(reservation.getRequestDate());
        assertThat(response.getReservationDate()).isEqualTo(reservation.getReservationDate());
        assertThat(response.getClientNumber()).isEqualTo(reservation.getClientNumber());
        assertThat(response.getPayment()).isEqualTo(reservation.getPayment());
        assertThat(response.getStatus()).isEqualTo(reservation.getStatus());
        assertThat(response.getPayDate()).isEqualTo(reservation.getPayDate());
        assertThat(response.getPrice()).isEqualTo(reservation.getPrice());
        assertThat(response.getOccasion()).isEqualTo(reservation.getOccasion());
        assertThat(response.getPetition()).isEqualTo(reservation.getPetition());
        assertThat(response.getByClient()).isEqualTo(reservation.getByClient());
    }

    @Test
    void shouldMapReservationDTOtoReservationEntity() {
        Branch branch = utils.createBranch(null);
        ReservationDTO dto = utils.createReservationDTO(utils.createReservation(branch));
        Reservation entity = reservationMapper.toEntity(dto, branch);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getBranch().getId()).isEqualTo(dto.getBranchId());
        assertThat(entity.getRequestDate()).isEqualTo(dto.getRequestDate());
        assertThat(entity.getReservationDate()).isEqualTo(dto.getReservationDate());
        assertThat(entity.getClientNumber()).isEqualTo(dto.getClientNumber());
        assertThat(entity.getPayment()).isEqualTo(dto.getPayment());
        assertThat(entity.getStatus()).isEqualTo(dto.getStatus());
        assertThat(entity.getPayDate()).isEqualTo(dto.getPayDate());
        assertThat(entity.getPrice()).isEqualTo(dto.getPrice());
        assertThat(entity.getOccasion()).isEqualTo(dto.getOccasion());
        assertThat(entity.getPetition()).isEqualTo(dto.getPetition());
        assertThat(entity.getByClient()).isEqualTo(dto.getByClient());
    }

    @Test
    void shouldPartiallyMapReservationDTOtoReservationEntity() {
        Reservation reservation = utils.createReservation(utils.createBranch(null));

        // Not changing ID
        ReservationDTO dto = ReservationDTO.builder()
                .id(reservation.getId() + 1)
                .build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getId()).isEqualTo(reservation.getId());

        // Not changing Branch ID
        dto = ReservationDTO.builder()
                .branchId(reservation.getBranch().getId() + 1)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getBranch().getId()).isEqualTo(reservation.getBranch().getId());

        // Changing requestDate
        dto = ReservationDTO.builder()
                .requestDate(new Date(System.currentTimeMillis()))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getRequestDate()).isEqualTo(dto.getRequestDate());

        // Changing ReservationDate
        dto = ReservationDTO.builder()
                .reservationDate(new Date(System.currentTimeMillis()))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getReservationDate()).isEqualTo(dto.getReservationDate());

        // Changing clientNumber
        dto = ReservationDTO.builder()
                .clientNumber(69)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getClientNumber()).isEqualTo(dto.getClientNumber());

        // Changing payment
        dto = ReservationDTO.builder()
                .payment("69")
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getPayment()).isEqualTo(dto.getPayment());

        // Changing status
        dto = ReservationDTO.builder()
                .status(1)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getStatus()).isEqualTo(dto.getStatus());

        // Changing payDate
        dto = ReservationDTO.builder()
                .payDate(new Date(System.currentTimeMillis()))
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getPayDate()).isEqualTo(dto.getPayDate());

        // Changing price
        dto = ReservationDTO.builder()
                .price(69.69F)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getPrice()).isEqualTo(dto.getPrice());

        // Changing occasion
        dto = ReservationDTO.builder()
                .occasion("Anniversary")
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getOccasion()).isEqualTo(dto.getOccasion());

        // Changing petition
        dto = ReservationDTO.builder()
                .petition("Candles")
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getPetition()).isEqualTo(dto.getPetition());

        // Changing byClient
        dto = ReservationDTO.builder()
                .byClient(Boolean.TRUE)
                .build();
        updatedReservation = reservationMapper.updateModel(dto, reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getByClient()).isEqualTo(dto.getByClient());
    }
}
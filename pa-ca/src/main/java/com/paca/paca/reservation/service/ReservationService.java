package com.paca.paca.reservation.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.user.model.User;
import com.paca.paca.user.repository.UserRepository;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.utils.ReservationMapper;

import lombok.RequiredArgsConstructor;

import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;

    private final ReservationRepository reservationRepository;

    private final BranchRepository branchRepository;

    private final ClientGroupRepository clientGroupRepository;
    private final BusinessRepository businessRepository;

    private final ClientRepository clientRepository;

    public ResponseEntity<ReservationListDTO> getAll() {
        List<ReservationDTO> response = new ArrayList<>();
        reservationRepository.findAll().forEach(reservation -> {
            ReservationDTO dto = reservationMapper.toDTO(reservation);
            dto.setBranchId(reservation.getBranch().getId());
            response.add(dto);
        });

        return ResponseEntity.ok(ReservationListDTO.builder().reservations(response).build());
    }

    public ResponseEntity<ReservationDTO> getById(Long id) throws NoContentException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Reservation with id " + id + " does not exists",
                        27));

        ReservationDTO dto = reservationMapper.toDTO(reservation);
        dto.setBranchId(reservation.getBranch().getId());
        return new ResponseEntity<ReservationDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<ReservationDTO> save(ReservationDTO dto) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    20);
        }

        if (dto.getPrice() == null) {
            dto.setStatus(ReservationStatics.Status.paid);
        }

        Reservation newReservation = reservationMapper.toEntity(dto);
        newReservation.setBranch(branch.get());
        newReservation = reservationRepository.save(newReservation);

        ReservationDTO newDto = reservationMapper.toDTO(newReservation);
        newDto.setBranchId(newReservation.getBranch().getId());

        return new ResponseEntity<ReservationDTO>(newDto, HttpStatus.OK);
    }

    public ResponseEntity<ReservationDTO> update(Long id, ReservationDTO dto) throws NoContentException {
        Optional<Reservation> current = reservationRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id: " + id + " does not exists",
                    27);
        }

        Reservation newReservation = reservationMapper.updateModel(current.get(), dto);
        newReservation = reservationRepository.save(newReservation);
        ReservationDTO newDto = reservationMapper.toDTO(newReservation);
        newDto.setBranchId(newReservation.getBranch().getId());

        return new ResponseEntity<ReservationDTO>(newDto, HttpStatus.OK);
    }

    public void delete(Long id) throws NoContentException {
        Optional<Reservation> current = reservationRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id: " + id + " does not exists",
                    27);
        }
        reservationRepository.deleteById(id);
    }

    public void reject(Long id, String userEmail) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) throw new NoContentException("Reservation with id " + id + "does not exists");

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)){
            throw new BadRequestException("This reservation can't be rejected because it is already returned");
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)){
            throw new BadRequestException("This reservation can't be rejected because it is already closed");
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)){
            throw new BadRequestException("This reservation can't be rejected because it is already returned");
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id" + branchId + "does not exists");
        }

        Optional<Business> owner = businessRepository.findBusinessByUserEmail(userEmail);
        if (owner.isEmpty()) {
            throw new NoContentException("Business related to user with email" + userEmail + "does not exists");
        }

        Long businessId = branch.get().getBusiness().getId();
        if (businessId.equals(owner.get().getId())) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.rejected).build();
        Reservation updatedReservation = reservationMapper.updateModel(reservation.get(), dto);
        reservationRepository.save(updatedReservation);
    }

    public void pay(Long id, String userEmail) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) throw new NoContentException("Reservation with id " + id + "does not exists");

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)){
            throw new BadRequestException("This reservation can't be paid because it is already returned");
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)){
            throw new BadRequestException("This reservation can't be paid because it is already closed");
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)){
            throw new BadRequestException("This reservation can't be paid because it is already returned");
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.paid)){
            throw new BadRequestException("This reservation can't be paid because it is already paid");
        }

        Optional<Client> owner = clientRepository.findClientByUserEmail(userEmail);
        if (owner.isEmpty()) {
            throw new NoContentException("Client related to user with email" + userEmail + "does not exists");
        }

        Long ownerId = owner.get().getId();
        Long reservationId = reservation.get().getId();
        Optional<ClientGroup> group = clientGroupRepository.findByClientIdAndReservationId(ownerId, reservationId);
        if (group.isEmpty()) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.paid).build();
        Reservation updatedReservation = reservationMapper.updateModel(reservation.get(), dto);
        reservationRepository.save(updatedReservation);
    }

}
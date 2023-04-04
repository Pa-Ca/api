package com.paca.paca.reservation.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.reservation.dto.ReservationPaymentDTO;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
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

    private final InvoiceRepository invoiceRepository;

    public ReservationListDTO getAll() {
        List<ReservationDTO> response = new ArrayList<>();
        reservationRepository.findAll().forEach(reservation -> {
            ReservationDTO dto = reservationMapper.toDTO(reservation);
            response.add(dto);
        });

        return ReservationListDTO.builder().reservations(response).build();
    }

    public ReservationDTO getById(Long id) throws NoContentException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Reservation with id " + id + " does not exists",
                        27));

        ReservationDTO dto = reservationMapper.toDTO(reservation);
        return dto;
    }

    public ReservationDTO save(ReservationDTO dto) throws NoContentException {
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    20);
        }

        if (dto.getPrice() == null) {
            dto.setStatus(ReservationStatics.Status.paid);
        }

        Reservation newReservation = reservationMapper.toEntity(dto, branch.get());
        newReservation = reservationRepository.save(newReservation);

        ReservationDTO dtoResponse = reservationMapper.toDTO(newReservation);

        return dtoResponse;
    }

    public ReservationDTO update(Long id, ReservationDTO dto) throws NoContentException {
        Optional<Reservation> current = reservationRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists",
                    27);
        }

        Reservation newReservation = reservationMapper.updateModel(dto, current.get());
        newReservation = reservationRepository.save(newReservation);
        ReservationDTO dtoResponse = reservationMapper.toDTO(newReservation);

        return dtoResponse;
    }

    public void delete(Long id) throws NoContentException {
        Optional<Reservation> current = reservationRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists",
                    27);
        }
        reservationRepository.deleteById(id);
    }

    public void cancel(Long id, String userEmail) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()){
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists",
                    27);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.canceled)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already canceled", 72);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
        }

        Optional<Business> owner = businessRepository.findByUserEmail(userEmail);
        if (owner.isEmpty()) {
            throw new NoContentException("Business related to user with email " + userEmail + " does not exists", 74);
        }

        Long businessId = branch.get().getBusiness().getId();
        if (!businessId.equals(owner.get().getId())) {
            throw new ForbiddenException("Unauthorized access for this operation", 75);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.canceled).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void accept(Long id, String userEmail) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) throw new NoContentException(
                "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.accepted)){
            throw new BadRequestException("" +
                    "Reservation with id " + id + " can't be accepted because it is already accepted", 76);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.paid)){
            Long branchId = reservation.get().getBranch().getId();
            Optional<Branch> branch = branchRepository.findById(branchId);
            if (branch.isEmpty()) {
                throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
            }

            Optional<Business> owner = businessRepository.findByUserEmail(userEmail);
            if (owner.isEmpty()) {
                throw new NoContentException("Business related to user with email " + userEmail + " does not exists", 74);
            }

            Long businessId = branch.get().getBusiness().getId();
            if (!businessId.equals(owner.get().getId())) {
                throw new ForbiddenException("Unauthorized access for this operation");
            }

            ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.accepted).build();
            Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
            reservationRepository.save(updatedReservation);
        }
    }

    public void reject(Long id, String userEmail) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) throw new NoContentException(
                "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already rejected", 71);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
        }

        Optional<Business> owner = businessRepository.findByUserEmail(userEmail);
        if (owner.isEmpty()) {
            throw new NoContentException("Business related to user with email " + userEmail + " does not exists", 74);
        }

        Long businessId = branch.get().getBusiness().getId();
        if (businessId.equals(owner.get().getId())) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.rejected).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void pay(Long id, String userEmail, ReservationPaymentDTO dto)
            throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) throw new NoContentException(
                "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.paid)){
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already paid", 77);
        }

        Optional<Client> owner = clientRepository.findByUserEmail(userEmail);
        if (owner.isEmpty()) {
            throw new NoContentException(
                    "Client related to user with email " + userEmail + " does not exists", 78);
        }

        Long ownerId = owner.get().getId();
        Long reservationId = reservation.get().getId();
        Optional<ClientGroup> group = clientGroupRepository.findByClientIdAndReservationId(ownerId, reservationId);
        if (group.isEmpty()) {
            throw new NoContentException(
                    "Client Group related to client with id " + ownerId +
                            " and reservation with id "+ reservationId + " does not exists", 79);
        }

        ReservationDTO changes = ReservationDTO.builder().status(ReservationStatics.Status.paid).build();
        Reservation updatedReservation = reservationMapper.updateModel(changes, reservation.get());
        reservationRepository.save(updatedReservation);

        Invoice invoice = Invoice.builder()
                .reservation(reservation.get())
                .price(reservation.get().getPrice())
                .payment(reservation.get().getPayment())
                .clientNumber(reservation.get().getClientNumber())
                .paymentCode(dto.getPaymentCode())
                .build();
        invoiceRepository.save(invoice);
    }

}
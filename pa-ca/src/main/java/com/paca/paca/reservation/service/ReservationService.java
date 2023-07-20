package com.paca.paca.reservation.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.reservation.dto.ReservationPaymentDTO;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final GuestMapper guestMapper;

    private final ReservationMapper reservationMapper;

    private final GuestRepository guestRepository;

    private final BranchRepository branchRepository;

    private final ClientRepository clientRepository;

    private final InvoiceRepository invoiceRepository;

    private final ReservationRepository reservationRepository;

    private final ClientGroupRepository clientGroupRepository;

    public ReservationListDTO getAll() {
        List<ReservationDTO> response = new ArrayList<>();
        reservationRepository.findAll().forEach(reservation -> {
            ReservationDTO dto = reservationMapper.toDTO(reservation);
            response.add(dto);
        });

        // Complete reservations
        List<ReservationDTO> result = response.stream().map(reservation -> {
            reservation.completeData(guestRepository, clientGroupRepository, clientRepository);
            return reservation;
        }).collect(Collectors.toList());

        return ReservationListDTO.builder().reservations(result).build();
    }

    public ReservationDTO getById(Long id) throws NoContentException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Reservation with id " + id + " does not exists",
                        27));

        ReservationDTO dto = reservationMapper.toDTO(reservation);
        dto.completeData(guestRepository, clientGroupRepository, clientRepository);
        return dto;
    }

    public ReservationDTO save(ReservationDTO dto) throws NoContentException, BadRequestException {
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    20);
        }

        if (dto.getPrice() == null) {
            dto.setStatus(ReservationStatics.Status.paid);
        }

        if (dto.getByClient() && dto.getClientNumber() > branch.get().getCapacity()) {
            throw new BadRequestException(
                    "Requested number of client surpass branch " + dto.getBranchId() + " capacity",
                    20);
        }

        Reservation newReservation;
        if (dto.getHaveGuest()) {
            GuestDTO guestDTO = GuestDTO.fromReservationDTO(dto);
            Optional<Guest> guestDB = guestRepository.findByIdentityDocument(
                    guestDTO.getIdentityDocument());
            Guest guest;
            if (guestDB.isPresent()) {
                guest = guestMapper.updateModel(guestDTO, guestDB.get());
            } else {
                guest = guestMapper.toEntity(guestDTO);
            }
            guest = guestRepository.save(guest);
            newReservation = reservationMapper.toEntity(dto, branch.get(), guest);
        } else {
            newReservation = reservationMapper.toEntity(dto, branch.get());
        }

        newReservation = reservationRepository.save(newReservation);

        ReservationDTO dtoResponse = reservationMapper.toDTO(newReservation);
        dtoResponse.completeData(guestRepository, clientGroupRepository, clientRepository);

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
        dtoResponse.completeData(guestRepository, clientGroupRepository, clientRepository);

        return dtoResponse;
    }

    public void cancel(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists",
                    27);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.canceled)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be canceled because it is already canceled", 72);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.canceled).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void reject(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already rejected", 71);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.rejected).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void accept(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.accepted)) {
            throw new BadRequestException("" +
                    "Reservation with id " + id + " can't be accepted because it is already accepted", 76);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists",
                    73);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.accepted).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);

        // For Payment
        /*
         * if (reservation.get().getStatus().equals(ReservationStatics.Status.paid)) {
         * Long branchId = reservation.get().getBranch().getId();
         * Optional<Branch> branch = branchRepository.findById(branchId);
         * 
         * 
         * if (branch.isEmpty()) {
         * throw new NoContentException("Branch related to reservation with id " +
         * branchId + " does not exists",
         * 73);
         * }
         * 
         * Optional<Business> owner = businessRepository.findByUserEmail(userEmail);
         * if (owner.isEmpty()) {
         * throw new NoContentException("Business related to user with email " +
         * userEmail + " does not exists",
         * 74);
         * }
         * 
         * Long businessId = branch.get().getBusiness().getId();
         * if (!businessId.equals(owner.get().getId())) {
         * throw new ForbiddenException("Unauthorized access for this operation");
         * }
         * 
         * ReservationDTO dto =
         * ReservationDTO.builder().status(ReservationStatics.Status.accepted).build();
         * Reservation updatedReservation = reservationMapper.updateModel(dto,
         * reservation.get());
         * reservationRepository.save(updatedReservation);
         * }
         */
    }

    public void start(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already rejected", 71);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.started).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void retire(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already rejected", 71);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.retired).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void close(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be closed because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be closed because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be closed because it is already rejected", 71);
        }

        Long branchId = reservation.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException("Branch related to reservation with id " + branchId + " does not exists", 73);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.closed).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void pay(Long id, ReservationPaymentDTO dto)
            throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.returned)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.closed)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.rejected)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.paid)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already paid", 77);
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
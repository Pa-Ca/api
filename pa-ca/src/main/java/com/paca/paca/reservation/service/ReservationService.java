package com.paca.paca.reservation.service;

import java.util.Set;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.ArrayList;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.dto.InvoiceDTO;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.utils.InvoiceMapper;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationInfoDTO;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.reservation.dto.BranchReservationsInfoDTO;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.reservation.repository.ClientGroupRepository;
import com.paca.paca.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.support.PagedListHolder;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final GuestMapper guestMapper;

    private final ClientMapper clientMapper;

    private final InvoiceMapper invoiceMapper;

    private final ReservationMapper reservationMapper;

    private final GuestRepository guestRepository;

    private final BranchRepository branchRepository;

    private final ClientRepository clientRepository;

    private final InvoiceRepository invoiceRepository;

    private final ReservationRepository reservationRepository;

    private final ClientGroupRepository clientGroupRepository;

    public ReservationInfoDTO getById(Long id) throws NoContentException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Reservation with id " + id + " does not exists",
                        27));

        return new ReservationInfoDTO(
                reservation,
                guestRepository,
                clientRepository,
                invoiceRepository,
                clientGroupRepository,
                guestMapper,
                clientMapper,
                invoiceMapper,
                reservationMapper);
    }

    public ReservationInfoDTO save(ReservationInfoDTO dto) throws NoContentException, BadRequestException {
        ReservationDTO reservationDTO = dto.getReservation();
        Long branchId = reservationDTO.getBranchId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        if (reservationDTO.getByClient() && reservationDTO.getClientNumber() > branch.get().getCapacity()) {
            throw new BadRequestException(
                    "Requested number of client surpass branch " + reservationDTO.getBranchId() + " capacity",
                    20);
        }

        Reservation newReservation;
        if (!reservationDTO.getByClient()) {
            GuestDTO guestDTO = dto.getGuest();
            Optional<Guest> guestDB = guestRepository.findByIdentityDocument(
                    guestDTO.getIdentityDocument());
            Guest guest;
            if (guestDB.isPresent()) {
                guest = guestMapper.updateModel(guestDTO, guestDB.get());
            } else {
                guest = guestMapper.toEntity(guestDTO);
            }
            guest = guestRepository.save(guest);
            newReservation = reservationMapper.toEntity(reservationDTO, branch.get(), guest);
        } else {
            newReservation = reservationMapper.toEntity(reservationDTO, branch.get());
        }

        newReservation = reservationRepository.save(newReservation);
        ReservationInfoDTO dtoResponse = new ReservationInfoDTO(
                newReservation,
                guestRepository,
                clientRepository,
                invoiceRepository,
                clientGroupRepository,
                guestMapper,
                clientMapper,
                invoiceMapper,
                reservationMapper);

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

    public ReservationInfoDTO update(Long id, ReservationDTO dto) throws NoContentException {
        Optional<Reservation> current = reservationRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists",
                    27);
        }

        Reservation newReservation = reservationMapper.updateModel(dto, current.get());
        newReservation = reservationRepository.save(newReservation);
        ReservationInfoDTO dtoResponse = new ReservationInfoDTO(
                newReservation,
                guestRepository,
                clientRepository,
                invoiceRepository,
                clientGroupRepository,
                guestMapper,
                clientMapper,
                invoiceMapper,
                reservationMapper);

        return dtoResponse;
    }

    public BranchReservationsInfoDTO getBranchReservations(
            int page,
            int size,
            Long branchId,
            List<Short> status,
            Date startTime,
            Date endTime,
            String fullname,
            String identityDocument)
            throws UnprocessableException, NoContentException {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }
        if (page < 0) {
            throw new UnprocessableException(
                    "Page number cannot be less than zero",
                    44);
        }
        if (size < 1) {
            throw new UnprocessableException(
                    "Page size cannot be less than one",
                    45);
        }

        if (status == null) {
            status = List.of(
                    ReservationStatics.Status.REJECTED,
                    ReservationStatics.Status.CLOSED,
                    ReservationStatics.Status.RETURNED,
                    ReservationStatics.Status.RETIRED);
        }

        // Apply filter to the historical reservations
        Set<Reservation> historicReservationsConcat = new HashSet<>();
        List<Reservation> historicReservationsByWord = reservationRepository.findAllByBranchIdAndFilters(
                branchId,
                status,
                startTime,
                endTime,
                null,
                null,
                identityDocument);
        // Add the reservations to the set
        historicReservationsConcat.addAll(historicReservationsByWord);
        if (fullname != null) {
            // Separate the fullname in tokens to apply the filters dynamically
            for (String word : fullname.split(" ")) {
                historicReservationsByWord = reservationRepository.findAllByBranchIdAndFilters(
                        branchId,
                        status,
                        startTime,
                        endTime,
                        word,
                        null,
                        identityDocument);
                historicReservationsByWord.addAll(reservationRepository.findAllByBranchIdAndFilters(
                        branchId,
                        status,
                        startTime,
                        endTime,
                        null,
                        word,
                        identityDocument));
                historicReservationsConcat.retainAll(historicReservationsByWord);
            }

        }
        // Create list from set
        List<Reservation> historicReservations = new ArrayList<>(historicReservationsConcat);
        // Sort the list
        historicReservations.sort((s1, s2) -> s2.getReservationDateIn().compareTo(s1.getReservationDateIn()));

        // Create a Pageable object for the historic reservations
        PagedListHolder<Reservation> historicReservationsPage = new PagedListHolder<Reservation>(historicReservations);
        historicReservationsPage.setPageSize(size);
        historicReservationsPage.setPage(page);

        // Map the results to a list of ReservationDTO objects using the
        // ReservationMapper
        List<ReservationInfoDTO> historicReservationsDTO = new ArrayList<>();
        historicReservationsPage.getPageList().forEach(reservation -> {
            historicReservationsDTO.add(new ReservationInfoDTO(
                    reservation,
                    guestRepository,
                    clientRepository,
                    invoiceRepository,
                    clientGroupRepository,
                    guestMapper,
                    clientMapper,
                    invoiceMapper,
                    reservationMapper));
        });

        // Get started reservations
        List<Reservation> startedReservations = reservationRepository.findAllByBranchIdAndFilters(
                branchId,
                List.of(ReservationStatics.Status.STARTED),
                null,
                null,
                null,
                null,
                null);
        // Map the results to a list of ReservationDTO objects using the
        // ReservationMapper
        List<ReservationInfoDTO> startedReservationsDTO = new ArrayList<>();
        startedReservations.forEach(reservation -> {
            startedReservationsDTO.add(new ReservationInfoDTO(
                    reservation,
                    guestRepository,
                    clientRepository,
                    invoiceRepository,
                    clientGroupRepository,
                    guestMapper,
                    clientMapper,
                    invoiceMapper,
                    reservationMapper));
        });

        // Get accepted reservations
        List<Reservation> acceptedReservations = reservationRepository.findAllByBranchIdAndFilters(
                branchId,
                List.of(ReservationStatics.Status.ACCEPTED),
                null,
                null,
                null,
                null,
                null);
        // Map the results to a list of ReservationDTO objects using the
        // ReservationMapper
        List<ReservationInfoDTO> acceptedReservationsDTO = new ArrayList<>();
        acceptedReservations.forEach(reservation -> {
            acceptedReservationsDTO.add(new ReservationInfoDTO(
                    reservation,
                    guestRepository,
                    clientRepository,
                    invoiceRepository,
                    clientGroupRepository,
                    guestMapper,
                    clientMapper,
                    invoiceMapper,
                    reservationMapper));
        });

        // Get pending reservations
        List<Reservation> pendingReservations = reservationRepository.findAllByBranchIdAndFilters(
                branchId,
                List.of(ReservationStatics.Status.PENDING),
                null,
                null,
                null,
                null,
                null);
        // Map the results to a list of ReservationDTO objects using the
        // ReservationMapper
        List<ReservationInfoDTO> pendingReservationsDTO = new ArrayList<>();
        pendingReservations.forEach(reservation -> {
            pendingReservationsDTO.add(new ReservationInfoDTO(
                    reservation,
                    guestRepository,
                    clientRepository,
                    invoiceRepository,
                    clientGroupRepository,
                    guestMapper,
                    clientMapper,
                    invoiceMapper,
                    reservationMapper));
        });

        // Return a ReservationListDTO object that contains the list of ReservationDTO
        // objects
        return BranchReservationsInfoDTO.builder()
                .startedReservations(startedReservationsDTO)
                .acceptedReservations(acceptedReservationsDTO)
                .pendingReservations(pendingReservationsDTO)
                .historicReservations(historicReservationsDTO)
                .currentHistoricPage(page)
                .totalHistoricPages(historicReservationsPage.getPageCount())
                .totalHistoricElements(historicReservations.size())
                .build();
    }

    public void cancel(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists",
                    27);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.RETURNED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be cancelled because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be cancelled because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.REJECTED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be cancelled because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CANCELED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be cancelled because it is already cancelled", 72);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.CANCELED).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void reject(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.RETURNED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.REJECTED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be rejected because it is already rejected", 71);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.REJECTED).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void accept(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty()) {
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.RETURNED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.REJECTED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be accepted because it is already rejected", 71);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.ACCEPTED)) {
            throw new BadRequestException("" +
                    "Reservation with id " + id + " can't be accepted because it is already accepted", 76);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.ACCEPTED).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void start(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.RETURNED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't start because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't start because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.REJECTED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't start because it is already rejected", 71);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.STARTED).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void retire(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.RETURNED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be retired because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be retired because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.REJECTED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be retired because it is already rejected", 71);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.RETIRED).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public void close(Long id) throws NoContentException, BadRequestException, ForbiddenException {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.RETURNED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be closed because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be closed because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.REJECTED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be closed because it is already rejected", 71);
        }

        ReservationDTO dto = ReservationDTO.builder().status(ReservationStatics.Status.CLOSED).build();
        Reservation updatedReservation = reservationMapper.updateModel(dto, reservation.get());
        reservationRepository.save(updatedReservation);
    }

    public InvoiceDTO pay(Long id, InvoiceDTO dto)
            throws NoContentException, BadRequestException, ForbiddenException {
        dto.setPayDate(new Date());
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isEmpty())
            throw new NoContentException(
                    "Reservation with id " + id + " does not exists", 27);

        if (reservation.get().getStatus().equals(ReservationStatics.Status.RETURNED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already returned", 69);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already closed", 70);
        }

        if (reservation.get().getStatus().equals(ReservationStatics.Status.REJECTED)) {
            throw new BadRequestException(
                    "Reservation with id " + id + " can't be paid because it is already rejected", 71);
        }

        // Create invoice
        Invoice invoice = invoiceMapper.toEntity(dto);
        invoice = invoiceRepository.save(invoice);

        // Update reservation
        Reservation updatedReservation = reservation.get();
        updatedReservation.setInvoice(invoice);
        updatedReservation = reservationRepository.save(updatedReservation);

        return invoiceMapper.toDTO(invoice);
    }

}
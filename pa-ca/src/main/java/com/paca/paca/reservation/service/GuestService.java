package com.paca.paca.reservation.service;

import java.util.Optional;

import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.reservation.dto.GuestInfoDTO;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestMapper guestMapper;

    private final GuestRepository guestRepository;

    private final ClientGuestRepository clientGuestRepository;

    private final ReservationRepository reservationRepository;

    public GuestInfoDTO getById(Long id) throws NoContentException {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Guest with id " + id + " does not exists",
                        54));

        GuestDTO dto = guestMapper.toDTO(guest);
        ClientGuest clientGuest = clientGuestRepository.findByGuestId(guest.getId()).get();

        return new GuestInfoDTO(dto, clientGuest.getId());
    }

    public GuestInfoDTO getByIdentityDocument(Long businessId, String identityDocument)
            throws NoContentException, ForbiddenException {

        Guest guest = guestRepository.findByIdentityDocument(identityDocument)
                .orElseThrow(() -> new NoContentException(
                        "Guest with identityDocument " + identityDocument + " does not exists",
                        40));

        if (!reservationRepository.existsByBranchBusinessIdAndGuestId(businessId, guest.getId())) {
            throw new ForbiddenException(
                    "Guest with identityDocument " + identityDocument +
                            " does not have a past reservation with this business",
                    40);
        }
        GuestDTO dto = guestMapper.toDTO(guest);
        ClientGuest clientGuest = clientGuestRepository.findByGuestId(guest.getId()).get();

        return new GuestInfoDTO(dto, clientGuest.getId());
    }

    public GuestInfoDTO save(GuestDTO dto) throws ConflictException {
        Optional<Guest> current = guestRepository.findByIdentityDocument(dto.getIdentityDocument());
        if (current.isPresent()) {
            throw new ConflictException(
                    "Guest with identity document " + dto.getIdentityDocument() + " already exists",
                    60);
        }

        current = guestRepository.findByEmail(dto.getEmail());
        if (current.isPresent()) {
            throw new ConflictException(
                    "Guest with email " + dto.getEmail() + " already exists",
                    61);
        }

        current = guestRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (current.isPresent()) {
            throw new ConflictException(
                    "Guest with phone number " + dto.getPhoneNumber() + " already exists",
                    62);
        }

        Guest newGuest = guestMapper.toEntity(dto);
        newGuest = guestRepository.save(newGuest);

        GuestDTO response = guestMapper.toDTO(newGuest);

        ClientGuest clientGuest = ClientGuest.builder()
                .client(null)
                .guest(newGuest)
                .haveGuest(true)
                .build();
        clientGuest = clientGuestRepository.save(clientGuest);

        return new GuestInfoDTO(response, clientGuest.getId());
    }

    public GuestInfoDTO update(Long id, GuestDTO dto) throws NoContentException {
        Optional<Guest> current = guestRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Guest with id " + id + " does not exists",
                    54);
        }

        Guest newGuest = guestMapper.updateModel(dto, current.get());
        newGuest = guestRepository.save(newGuest);
        GuestDTO dtoResponse = guestMapper.toDTO(newGuest);
        ClientGuest clientGuest = clientGuestRepository.findByGuestId(id).get();

        return new GuestInfoDTO(dtoResponse, clientGuest.getId());
    }

    public void delete(Long id) throws NoContentException {
        Optional<Guest> current = guestRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Guest with id " + id + " does not exists",
                    54);
        }
        guestRepository.deleteById(id);
    }
}

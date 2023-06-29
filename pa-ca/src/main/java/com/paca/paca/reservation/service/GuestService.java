package com.paca.paca.reservation.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.dto.GuestListDTO;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.exception.exceptions.NoContentException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestMapper guestMapper;

    private final GuestRepository guestRepository;

    public GuestListDTO getAll() {
        List<GuestDTO> response = new ArrayList<>();
        guestRepository.findAll().forEach(guest -> {
            GuestDTO dto = guestMapper.toDTO(guest);
            response.add(dto);
        });

        return GuestListDTO.builder().guests(response).build();
    }

    public GuestDTO getById(Long id) throws NoContentException {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Guest with id " + id + " does not exists",
                        40));

        GuestDTO dto = guestMapper.toDTO(guest);
        return dto;
    }

    public GuestDTO getByIdentityDocument(String identityDocument) throws NoContentException {
        Guest guest = guestRepository.findByIdentityDocument(identityDocument)
                .orElseThrow(() -> new NoContentException(
                        "Guest with identityDocument " + identityDocument + " does not exists",
                        40));

        GuestDTO dto = guestMapper.toDTO(guest);
        return dto;
    }

    public GuestDTO save(GuestDTO dto) throws NoContentException {
        Guest newGuest = guestMapper.toEntity(dto);
        newGuest = guestRepository.save(newGuest);

        GuestDTO dtoResponse = guestMapper.toDTO(newGuest);

        return dtoResponse;
    }

    public GuestDTO update(Long id, GuestDTO dto) throws NoContentException {
        Optional<Guest> current = guestRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Guest with id " + id + " does not exists",
                    40);
        }

        Guest newGuest = guestMapper.updateModel(dto, current.get());
        newGuest = guestRepository.save(newGuest);
        GuestDTO dtoResponse = guestMapper.toDTO(newGuest);

        return dtoResponse;
    }

    public void delete(Long id) throws NoContentException {
        Optional<Guest> current = guestRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Guest with id " + id + " does not exists",
                    40);
        }
        guestRepository.deleteById(id);
    }

}

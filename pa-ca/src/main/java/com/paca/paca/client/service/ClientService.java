package com.paca.paca.client.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import com.paca.paca.user.model.User;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.repository.ClientGroupRepository;

import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class ClientService {

    private final ReservationMapper reservationMapper;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final ClientGroupRepository clientGroupRepository;

    public ClientService(
            ReservationMapper reservationMapper,
            UserRepository userRepository,
            ClientRepository clientRepository,
            ClientGroupRepository clientGroupRepository) {
        this.reservationMapper = reservationMapper;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.clientGroupRepository = clientGroupRepository;
    }

    public ResponseEntity<Map<String, List<Client>>> getAll() {
        // Get clients
        List<Client> clients = clientRepository.findAll();

        // Create response
        Map<String, List<Client>> body = new HashMap<>();
        body.put("clients", clients);
        return new ResponseEntity<Map<String, List<Client>>>(body, HttpStatus.OK);
    }

    public ResponseEntity<Client> getById(Long id) throws NoContentException {
        // Get client
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Client with id: " + id + " does not exists",
                        11));

        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    public ResponseEntity<Client> getByUserEmail(String email) throws NoContentException {
        // Get client
        Client client = clientRepository.findByUserEmail(email)
                .orElseThrow(() -> new NoContentException(
                        "Client with user email: " + email + " does not exists",
                        11));

        // Create response
        return new ResponseEntity<Client>(client, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Long>> save(ClientDTO dto) throws NoContentException, ConflictException {
        String email = dto.getEmail();

        boolean userExists = this.userRepository.existsByEmail(email);
        if (!userExists) {
            throw new NoContentException("User does not exists", 8);
        }

        boolean clientExists = this.clientRepository.existsByUserEmail(email);
        if (clientExists) {
            throw new ConflictException("Client already exists", 12);
        }

        User user = this.userRepository.findByEmail(email).orElseThrow();

        Client client = Client.builder()
                .user(user)
                .name(dto.getName())
                .surname(dto.getSurname())
                .stripeCustomerId(dto.getStripeCustomerId())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .build();

        Client newClient = clientRepository.save(client);

        // Create response
        Map<String, Long> body = new HashMap<>();
        body.put("client", newClient.getId());
        return new ResponseEntity<Map<String, Long>>(body, HttpStatus.OK);
    }

    public void update(Long id, ClientDTO dto) throws NoContentException {
        Optional<Client> current = clientRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException("Client does not exists", 8);
        }

        User user = this.userRepository.findByEmail(dto.getEmail()).orElseThrow();
        Client client = Client.builder()
                .id(id)
                .user(user)
                .name(dto.getName())
                .surname(dto.getSurname())
                .stripeCustomerId(dto.getStripeCustomerId())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .build();

        clientRepository.save(client);
    }

    public void delete(Long id) throws NoContentException {
        Optional<Client> current = clientRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException("Client does not exists", 8);
        }
        clientRepository.deleteById(id);

    }

    public ResponseEntity<ReservationListDTO> getReservations(Long id) throws NoContentException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NoContentException(
                    "Client with id: " + id + " does not exists",
                    28);
        }

        List<ReservationDTO> response = new ArrayList<>();
        clientGroupRepository.findAllByClientId(id).forEach(group -> {
            ReservationDTO dto = reservationMapper.toDTO(group.getReservation());
            dto.setBranchId(group.getReservation().getBranch().getId());
            response.add(dto);
        });

        return ResponseEntity.ok(ReservationListDTO.builder().reservations(response).build());
    }

    public ResponseEntity<ReservationListDTO> getReservationsByDate(Long id, Date reservationDate)
            throws NoContentException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NoContentException(
                    "Client with id: " + id + " does not exists",
                    28);
        }

        List<ReservationDTO> response = new ArrayList<>();
        clientGroupRepository.findAllByClientIdAndReservationReservationDateGreaterThanEqual(id, reservationDate)
                .forEach(group -> {
                    ReservationDTO dto = reservationMapper.toDTO(group.getReservation());
                    dto.setBranchId(group.getReservation().getBranch().getId());
                    response.add(dto);
                });

        return ResponseEntity.ok(ReservationListDTO.builder().reservations(response).build());
    }
}
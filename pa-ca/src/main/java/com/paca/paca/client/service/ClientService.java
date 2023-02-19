package com.paca.paca.client.service;

import org.springframework.stereotype.Service;

import com.paca.paca.user.model.User;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.client.utils.FriendMapper;
import com.paca.paca.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.repository.ClientGroupRepository;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper clientMapper;

    private final FriendMapper friendMapper;

    private final ReservationMapper reservationMapper;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final FriendRepository friendRepository;

    private final ClientGroupRepository clientGroupRepository;

    public ClientListDTO getAll() {
        List<ClientDTO> response = new ArrayList<>();
        clientRepository.findAll().forEach(client -> {
            ClientDTO dto = clientMapper.toDTO(client);
            response.add(dto);
        });

        return ClientListDTO.builder().clients(response).build();
    }

    public ClientDTO getById(Long id) throws NoContentException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NoContentException(
                        "Client with id " + id + " does not exists",
                        28));

        ClientDTO dto = clientMapper.toDTO(client);
        return dto;
    }

    public ClientDTO save(ClientDTO dto) throws NoContentException, ConflictException {
        String email = dto.getEmail();

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isEmpty()) {
            throw new NoContentException(
                    "User with email " + dto.getEmail() + " does not exists",
                    30);
        }

        boolean clientExists = clientRepository.existsByUserEmail(email);
        if (clientExists) {
            throw new ConflictException(
                    "Client with email " + dto.getEmail() + " already exists",
                    12);
        }

        Client newClient = clientMapper.toEntity(dto);
        newClient.setUser(user.get());
        newClient = clientRepository.save(newClient);

        ClientDTO newDto = clientMapper.toDTO(newClient);

        return newDto;
    }

    public ClientDTO update(Long id, ClientDTO dto) throws NoContentException {
        Optional<Client> current = clientRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        Client newClient = clientMapper.updateModel(dto, current.get());
        newClient = clientRepository.save(newClient);
        ClientDTO newDto = clientMapper.toDTO(newClient);

        return newDto;
    }

    public void delete(Long id) throws NoContentException {
        Optional<Client> current = clientRepository.findById(id);
        if (current.isEmpty()) {
            throw new NoContentException(
                    "Client with id " + id + " does not exists",
                    28);
        }
        clientRepository.deleteById(id);

    }

    public ClientDTO getByUserId(Long id) throws NoContentException {
        Client client = clientRepository.findByUserId(id)
                .orElseThrow(() -> new NoContentException(
                        "User with id " + id + " does not exists",
                        12));

        ClientDTO dto = clientMapper.toDTO(client);

        return dto;
    }

    public ClientListDTO getPendingFriends(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NoContentException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        List<ClientDTO> response = new ArrayList<>();
        friendRepository.findAllByAddresserIdAndAcceptedFalseAndRejectedFalse(id).forEach(
                request -> {
                    ClientDTO dto = clientMapper.toDTO(request.getRequester());
                    response.add(dto);
                });

        return ClientListDTO.builder().clients(response).build();
    }

    public ClientListDTO getAcceptedFriends(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NoContentException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        List<ClientDTO> response = new ArrayList<>();
        friendRepository.findAllByAddresserIdAndAcceptedTrue(id).forEach(
                request -> {
                    ClientDTO dto = clientMapper.toDTO(request.getRequester());
                    response.add(dto);
                });
        friendRepository.findAllByRequesterIdAndAcceptedTrue(id).forEach(
                request -> {
                    ClientDTO dto = clientMapper.toDTO(request.getAddresser());
                    response.add(dto);
                });

        return ClientListDTO.builder().clients(response).build();
    }

    public ClientListDTO getRejectedFriends(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NoContentException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        List<ClientDTO> response = new ArrayList<>();
        friendRepository.findAllByAddresserIdAndRejectedTrue(id).forEach(
                request -> {
                    ClientDTO dto = clientMapper.toDTO(request.getRequester());
                    response.add(dto);
                });

        return ClientListDTO.builder().clients(response).build();
    }

    public FriendDTO friendRequest(Long requesterId, Long addresserId)
            throws NoContentException, ConflictException {
        if (requesterId == addresserId) {
            throw new ConflictException("Requester and addresser can not be the same", 28);
        }

        Optional<Client> requester = clientRepository.findById(requesterId);
        if (requester.isEmpty()) {
            throw new NoContentException(
                    "Requester with id: " + requesterId + " does not exists",
                    13);
        }

        Optional<Client> addresser = clientRepository.findById(addresserId);
        if (addresser.isEmpty()) {
            throw new NoContentException(
                    "Addresser with id: " + addresserId + " does not exists",
                    14);
        }

        Boolean requestExists = friendRepository.existsByRequesterIdAndAddresserId(requesterId, addresserId);
        if (requestExists) {
            throw new ConflictException("Friend request already exists", 15);
        }

        Friend request = Friend.builder()
                .requester(requester.get())
                .addresser(addresser.get())
                .accepted(false)
                .rejected(false)
                .build();
        request = friendRepository.save(request);

        FriendDTO newDto = friendMapper.toDTO(request);

        return newDto;
    }

    public FriendDTO acceptFriendRequest(Long requesterId, Long addresserId)
            throws NoContentException, ConflictException {
        Optional<Friend> request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId);
        if (request.isEmpty()) {
            throw new NoContentException(
                    "Friend request does not exists",
                    16);
        }

        Friend newRequest = request.get();

        if (newRequest.getAccepted()) {
            throw new ConflictException("Friend request already accepted", 17);
        }
        if (newRequest.getRejected()) {
            throw new ConflictException("Friend request already rejected", 18);
        }

        newRequest.setAccepted(true);
        newRequest = friendRepository.save(newRequest);

        FriendDTO newDto = friendMapper.toDTO(newRequest);

        return newDto;
    }

    public FriendDTO rejectFriendRequest(Long requesterId, Long addresserId)
            throws NoContentException, ConflictException {
        Optional<Friend> request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId);
        if (request.isEmpty()) {
            throw new NoContentException(
                    "Friend request does not exists",
                    16);
        }

        Friend newRequest = request.get();

        if (newRequest.getAccepted()) {
            throw new ConflictException("Friend request already accepted", 17);
        }
        if (newRequest.getRejected()) {
            throw new ConflictException("Friend request already rejected", 18);
        }

        newRequest.setRejected(true);
        newRequest = friendRepository.save(newRequest);

        FriendDTO newDto = friendMapper.toDTO(newRequest);

        return newDto;
    }

    public void deleteFriendRequest(Long requesterId, Long addresserId)
            throws NoContentException {
        Optional<Friend> request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId);
        if (request.isEmpty()) {
            throw new NoContentException(
                    "Friend request does not exists",
                    16);
        }
        friendRepository.deleteById(request.get().getId());
    }

    public ReservationListDTO getReservations(Long id) throws NoContentException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NoContentException(
                    "Client with id: " + id + " does not exists",
                    28);
        }

        List<ReservationDTO> response = new ArrayList<>();
        clientGroupRepository.findAllByClientId(id).forEach(group -> {
            ReservationDTO dto = reservationMapper.toDTO(group.getReservation());
            response.add(dto);
        });

        return ReservationListDTO.builder().reservations(response).build();
    }

    public ReservationListDTO getReservationsByDate(Long id, Date reservationDate)
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
                    response.add(dto);
                });

        return ReservationListDTO.builder().reservations(response).build();
    }
}
package com.paca.paca.client.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.paca.paca.user.model.User;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.dto.ClientInfoDTO;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.client.utils.FriendMapper;
import com.paca.paca.branch.utils.BranchMapper;
import com.paca.paca.client.model.FavoriteBranch;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.reservation.utils.InvoiceMapper;
import com.paca.paca.reservation.dto.ReservationInfoDTO;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.reservation.dto.ReservationInfoListDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.client.repository.FavoriteBranchRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientMapper clientMapper;

    private final FriendMapper friendMapper;

    private final GuestMapper guestMapper;

    private final BranchMapper branchMapper;

    private final InvoiceMapper invoiceMapper;

    private final ReservationMapper reservationMapper;

    private final UserRepository userRepository;

    private final GuestRepository guestRepository;

    private final ClientRepository clientRepository;

    private final FriendRepository friendRepository;

    private final BranchRepository branchRepository;

    private final InvoiceRepository invoiceRepository;

    private final ClientGuestRepository clientGuestRepository;

    private final ClientGroupRepository clientGroupRepository;

    private final FavoriteBranchRepository favoriteBranchRepository;

    public ClientDTO getById(Long id) throws NotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Client with id " + id + " does not exists",
                        28));

        ClientDTO dto = clientMapper.toDTO(client);
        return dto;
    }

    public ClientInfoDTO save(ClientDTO dto) throws NotFoundException, ConflictException {
        String email = dto.getEmail();

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isEmpty()) {
            throw new NotFoundException(
                    "User with email " + dto.getEmail() + " does not exists",
                    30);
        }

        boolean clientExists = clientRepository.existsByUserEmail(email);
        if (clientExists) {
            throw new ConflictException(
                    "Client with email " + dto.getEmail() + " already exists",
                    12);
        }

        Client newClient = clientMapper.toEntity(dto, user.get());
        newClient = clientRepository.save(newClient);

        ClientGuest clientGuest = ClientGuest.builder()
                .client(newClient)
                .guest(null)
                .haveGuest(false)
                .build();
        clientGuest = clientGuestRepository.save(clientGuest);
        ClientDTO response = clientMapper.toDTO(newClient);

        return new ClientInfoDTO(response, clientGuest.getId());
    }

    public ClientInfoDTO update(Long id, ClientDTO dto) throws NotFoundException {
        Optional<Client> current = clientRepository.findById(id);
        if (current.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        Client newClient = clientMapper.updateModel(dto, current.get());
        newClient = clientRepository.save(newClient);
        ClientDTO response = clientMapper.toDTO(newClient);
        ClientGuest clientGuest = clientGuestRepository.findByClientId(id).get();

        return new ClientInfoDTO(response, clientGuest.getId());
    }

    public void delete(Long id) throws NotFoundException {
        Optional<Client> current = clientRepository.findById(id);
        if (current.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + id + " does not exists",
                    28);
        }
        clientRepository.deleteById(id);

    }

    public ClientInfoDTO getByUserId(Long id) throws NotFoundException {
        Client client = clientRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with id " + id + " does not exists",
                        12));

        ClientDTO dto = clientMapper.toDTO(client);
        ClientGuest clientGuest = clientGuestRepository.findByClientId(client.getId()).get();

        return new ClientInfoDTO(dto, clientGuest.getId());
    }

    public ClientListDTO getPendingFriends(Long id) throws NotFoundException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NotFoundException(
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

    public ClientListDTO getAcceptedFriends(Long id) throws NotFoundException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NotFoundException(
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

    public ClientListDTO getRejectedFriends(Long id) throws NotFoundException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NotFoundException(
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
            throws NotFoundException, ConflictException {
        if (requesterId == addresserId) {
            throw new ConflictException("Requester and addresser can not be the same", 28);
        }

        Optional<Client> requester = clientRepository.findById(requesterId);
        if (requester.isEmpty()) {
            throw new NotFoundException(
                    "Requester with id: " + requesterId + " does not exists",
                    13);
        }

        Optional<Client> addresser = clientRepository.findById(addresserId);
        if (addresser.isEmpty()) {
            throw new NotFoundException(
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

        FriendDTO response = friendMapper.toDTO(request);

        return response;
    }

    public FriendDTO acceptFriendRequest(Long requesterId, Long addresserId)
            throws NotFoundException, ConflictException {
        Optional<Friend> request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId);
        if (request.isEmpty()) {
            throw new NotFoundException(
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

        FriendDTO response = friendMapper.toDTO(newRequest);

        return response;
    }

    public FriendDTO rejectFriendRequest(Long requesterId, Long addresserId)
            throws NotFoundException, ConflictException {
        Optional<Friend> request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId);
        if (request.isEmpty()) {
            throw new NotFoundException(
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

        FriendDTO response = friendMapper.toDTO(newRequest);

        return response;
    }

    public void deleteFriendRequest(Long requesterId, Long addresserId)
            throws NotFoundException {
        Optional<Friend> request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId);
        if (request.isEmpty()) {
            throw new NotFoundException(
                    "Friend request does not exists",
                    16);
        }
        friendRepository.deleteById(request.get().getId());
    }

    public ReservationInfoListDTO getReservations(Long id) throws NotFoundException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        List<ReservationInfoDTO> response = new ArrayList<>();
        clientGroupRepository.findAllByClientId(id).forEach(group -> {
            response.add(new ReservationInfoDTO(
                    group.getReservation(),
                    guestRepository,
                    clientRepository,
                    invoiceRepository,
                    clientGroupRepository,
                    guestMapper,
                    clientMapper,
                    invoiceMapper,
                    reservationMapper));
        });

        return ReservationInfoListDTO.builder().reservations(response).build();
    }

    public BranchListDTO getFavoriteBranches(Long id) throws NotFoundException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        List<BranchDTO> response = new ArrayList<>();
        favoriteBranchRepository.findAllByClientId(id).forEach(fav -> {
            BranchDTO dto = branchMapper.toDTO(fav.getBranch());
            dto.setBusinessId(fav.getBranch().getBusiness().getId());
            response.add(dto);
        });

        return BranchListDTO.builder().branches(response).build();
    }

    public BranchDTO addFavoriteBranch(Long id, Long branchId) throws NotFoundException, ConflictException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NotFoundException(
                    "Branch with id " + branchId + " does not exists",
                    20);
        }

        Boolean favExists = favoriteBranchRepository.existsByClientIdAndBranchId(id, branchId);
        if (favExists) {
            throw new ConflictException("Favorite branch already exists", 32);
        }

        FavoriteBranch fav = FavoriteBranch.builder()
                .client(client.get())
                .branch(branch.get())
                .build();
        fav = favoriteBranchRepository.save(fav);

        BranchDTO dto = branchMapper.toDTO(branch.get());
        dto.setBusinessId(branch.get().getBusiness().getId());

        return dto;
    }

    public void deleteFavoriteBranch(Long id, Long branchId) throws NotFoundException {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new NotFoundException(
                    "Client with id " + id + " does not exists",
                    28);
        }

        Optional<Branch> branch = branchRepository.findById(id);
        if (branch.isEmpty()) {
            throw new NotFoundException(
                    "Branch with id " + id + " does not exists",
                    20);
        }

        Boolean favExists = favoriteBranchRepository.existsByClientIdAndBranchId(id, branchId);
        if (!favExists) {
            throw new NotFoundException("Favorite branch does not exists", 33);
        }

        FavoriteBranch fav = favoriteBranchRepository.findByClientIdAndBranchId(id, branchId).get();
        favoriteBranchRepository.deleteById(fav.getId());
    }
}
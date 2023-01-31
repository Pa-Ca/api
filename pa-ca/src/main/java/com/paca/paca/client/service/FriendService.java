package com.paca.paca.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public FriendService(FriendRepository friendRepository, ClientRepository clientRepository) {
        this.friendRepository = friendRepository;
        this.clientRepository = clientRepository;
    }

    public ResponseEntity<Map<String, List<Client>>> getPendings(Long userId) {
        // Get clients
        List<Friend> friends = friendRepository.findByAddresserIdAndAcceptedFalseAndRejectedFalse(userId);
        List<Client> clients = friends.stream()
                .map(friend -> friend.getRequester())
                .collect(Collectors.toList());

        // Create response
        Map<String, List<Client>> body = new HashMap<>();
        body.put("pending_requests", clients);
        return new ResponseEntity<Map<String, List<Client>>>(body, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, List<Client>>> getAccepted(Long userId) {
        // Get clients
        List<Friend> friends1 = friendRepository.findByAddresserIdAndAcceptedTrue(userId);
        List<Client> clients1 = friends1.stream()
                .map(friend -> friend.getRequester())
                .collect(Collectors.toList());

        List<Friend> friends2 = friendRepository.findByRequesterIdAndAcceptedTrue(userId);
        List<Client> clients2 = friends2.stream()
                .map(friend -> friend.getAddresser())
                .collect(Collectors.toList());
        clients1.addAll(clients2);

        // Create response
        Map<String, List<Client>> body = new HashMap<>();
        body.put("friends", clients1);
        return new ResponseEntity<Map<String, List<Client>>>(body, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, List<Client>>> getRejected(Long userId) {
        // Get clients
        List<Friend> rejected = friendRepository.findByAddresserIdAndRejectedTrue(userId);
        List<Client> clients = rejected.stream()
                .map(r -> r.getRequester())
                .collect(Collectors.toList());

        // Create response
        Map<String, List<Client>> body = new HashMap<>();
        body.put("rejected", clients);
        return new ResponseEntity<Map<String, List<Client>>>(body, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Long>> save(Long requesterId, Long addresserId)
            throws NoContentException, ConflictException {
        if (requesterId == addresserId) {
            throw new ConflictException("Requester and addresser can not be the same", 19);
        }

        Boolean requesterExists = clientRepository.existsById(requesterId);
        if (!requesterExists) {
            throw new NoContentException("Requester does not exists", 13);
        }
        Client requester = clientRepository.findById(requesterId).orElseThrow();

        Boolean addresserExists = clientRepository.existsById(addresserId);
        if (!addresserExists) {
            throw new NoContentException("Addresser does not exists", 14);
        }
        Client addreser = clientRepository.findById(addresserId).orElseThrow();

        Boolean requestExists = friendRepository.existsByRequesterIdAndAddresserId(requesterId, addresserId);
        if (requestExists) {
            throw new ConflictException("Friend request already exists", 15);
        }

        Friend request = Friend.builder()
                .requester(requester)
                .addresser(addreser)
                .accepted(false)
                .rejected(false)
                .build();
        Friend newRequest = friendRepository.save(request);

        // Create response
        Map<String, Long> body = new HashMap<>();
        body.put("request", newRequest.getId());
        return new ResponseEntity<Map<String, Long>>(body, HttpStatus.OK);
    }

    public void accept(Long requesterId, Long addresserId)
            throws NoContentException, ConflictException {
        Boolean requestExists = friendRepository.existsByRequesterIdAndAddresserId(requesterId, addresserId);
        if (!requestExists) {
            throw new ConflictException("Friend request does not exists", 16);
        }

        Friend request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId).orElseThrow();

        if (request.getAccepted()) {
            throw new ConflictException("Friend request already accepted", 17);
        }
        if (request.getRejected()) {
            throw new ConflictException("Friend request already rejected", 18);
        }

        request.setAccepted(true);
        friendRepository.save(request);
    }

    public void reject(Long requesterId, Long addresserId)
            throws NoContentException, ConflictException {
        Boolean requestExists = friendRepository.existsByRequesterIdAndAddresserId(requesterId, addresserId);
        if (!requestExists) {
            throw new NoContentException("Friend request does not exists", 16);
        }

        Friend request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId).orElseThrow();

        if (request.getAccepted()) {
            throw new ConflictException("Friend request already accepted", 17);
        }
        if (request.getRejected()) {
            throw new ConflictException("Friend request already rejected", 18);
        }

        request.setRejected(true);
        friendRepository.save(request);
    }

    public void delete(Long requesterId, Long addresserId)
            throws NoContentException {
        Boolean requestExists = friendRepository.existsByRequesterIdAndAddresserId(requesterId, addresserId);
        if (!requestExists) {
            throw new NoContentException("Friend request does not exists", 16);
        }

        Friend request = friendRepository.findByRequesterIdAndAddresserId(requesterId, addresserId).orElseThrow();
        friendRepository.deleteById(request.getId());
    }
}
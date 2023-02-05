package com.paca.paca.client.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.client.statics.ClientStatics;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping(ClientStatics.Endpoint.PATH)
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<ClientListDTO> getAll() {
        return clientService.getAll();
    }

    @PostMapping
    public ResponseEntity<ClientDTO> save(@RequestBody ClientDTO client)
            throws NoContentException, ConflictException {
        return clientService.save(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return clientService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@PathVariable("id") Long id, @RequestBody ClientDTO client)
            throws NoContentException {
        return clientService.update(id, client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        clientService.delete(id);
    }

    @PostMapping("/{id}/friend")
    public ResponseEntity<FriendDTO> friendRequest(
            @PathVariable("id") Long id,
            @RequestBody FriendDTO request) throws NoContentException, ConflictException {
        return clientService.friendRequest(request.getRequesterId(), id);
    }

    @GetMapping("/{id}/friend/accepted")
    public ResponseEntity<ClientListDTO> getAcceptedFriends(@PathVariable("id") Long id) {
        return clientService.getAcceptedFriends(id);
    }

    @GetMapping("/{id}/friend/rejected")
    public ResponseEntity<ClientListDTO> getRejectedFriends(@PathVariable("id") Long id) {
        return clientService.getRejectedFriends(id);
    }

    @GetMapping("/{id}/friend/pending")
    public ResponseEntity<ClientListDTO> getPendingFriends(@PathVariable("id") Long id) {
        return clientService.getPendingFriends(id);
    }

    @DeleteMapping("/{id}/friend/pending/{requesterId}")
    public void deleteFriendRequest(@PathVariable("id") Long id, @PathVariable("requesterId") Long requesterId)
            throws NoContentException {
        clientService.deleteFriendRequest(requesterId, id);
    }

    @PutMapping("/{id}/friend/pending/{requesterId}/accept")
    public ResponseEntity<FriendDTO> acceptFriendRequest(@PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        return clientService.acceptFriendRequest(requesterId, id);
    }

    @PutMapping("/{id}/friend/pending/{requesterId}/reject")
    public ResponseEntity<FriendDTO> rejectFriendRequest(@PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        return clientService.rejectFriendRequest(requesterId, id);
    }

    @GetMapping("/{id}/reservation")
    public ResponseEntity<ReservationListDTO> getReservations(@PathVariable("id") Long id) throws NoContentException {
        return clientService.getReservations(id);
    }

    @GetMapping("/{id}/reservation/{date}")
    public ResponseEntity<ReservationListDTO> getReservationsByDate(
            @PathVariable("id") Long id,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date)
            throws NoContentException {
        return clientService.getReservationsByDate(id, date);
    }
}

package com.paca.paca.client.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendRequestDTO;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.client.service.FriendService;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.Map;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final ClientService clientService;

    private final FriendService friendService;

    public ClientController(ClientService clientService, FriendService friendService) {
        this.clientService = clientService;
        this.friendService = friendService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Client>>> getAll() {
        return clientService.getAll();
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> save(@RequestBody ClientDTO client)
            throws NoContentException, ConflictException {
        return clientService.save(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable("id") Long id) throws NoContentException {
        return clientService.getById(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Client> getByEmail(@PathVariable("email") String email)
            throws NoContentException {
        return clientService.getByUserEmail(email);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody ClientDTO client) throws NoContentException {
        clientService.update(id, client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        clientService.delete(id);
    }

    @PostMapping("/{id}/friend")
    public ResponseEntity<Map<String, Long>> sendRequest(
            @PathVariable("id") Long id,
            @RequestBody FriendRequestDTO request) throws NoContentException, ConflictException {
        return friendService.save(request.getRequesterId(), id);
    }

    @GetMapping("/{id}/friend/accepted")
    public ResponseEntity<Map<String, List<Client>>> getAccepted(@PathVariable("id") Long id) {
        return friendService.getAccepted(id);
    }

    @GetMapping("/{id}/friend/rejected")
    public ResponseEntity<Map<String, List<Client>>> getRejected(@PathVariable("id") Long id) {
        return friendService.getRejected(id);
    }

    @GetMapping("/{id}/friend/pending")
    public ResponseEntity<Map<String, List<Client>>> getPendings(@PathVariable("id") Long id) {
        return friendService.getPendings(id);
    }

    @DeleteMapping("/{id}/friend/pending/{requesterId}")
    public void delete(@PathVariable("id") Long id, @PathVariable("requesterId") Long requesterId)
            throws NoContentException {
        friendService.delete(requesterId, id);
    }

    @PutMapping("/{id}/friend/pending/{requesterId}/accept")
    public void accept(@PathVariable("id") Long id, @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        friendService.accept(requesterId, id);
    }

    @PutMapping("/{id}/friend/pending/{requesterId}/reject")
    public void reject(@PathVariable("id") Long id, @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        friendService.reject(requesterId, id);
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

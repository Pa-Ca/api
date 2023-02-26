package com.paca.paca.client.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.client.statics.ClientStatics;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.client.utils.ValidateClientInterceptor.ValidateClient;

import lombok.RequiredArgsConstructor;

import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.Date;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(ClientStatics.Endpoint.PATH)
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<ClientListDTO> getAll() {
        return ResponseEntity.ok(clientService.getAll());
    }

    @PostMapping
    @ValidateRoles({"client"})
    public ResponseEntity<ClientDTO> save(@RequestBody ClientDTO client)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.save(client));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @ValidateClient
    @PutMapping("/{id}")
    @ValidateRoles({"client"})
    public ResponseEntity<ClientDTO> update(@PathVariable("id") Long id, @RequestBody ClientDTO client)
            throws NoContentException {
        return ResponseEntity.ok(clientService.update(id, client));
    }

    @ValidateClient
    @DeleteMapping("/{id}")
    @ValidateRoles({"client"})
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        clientService.delete(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ClientDTO> getByUserId(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getByUserId(id));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @GetMapping("/{id}/friend/accepted")
    public ResponseEntity<ClientListDTO> getAcceptedFriends(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientService.getAcceptedFriends(id));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @GetMapping("/{id}/friend/rejected")
    public ResponseEntity<ClientListDTO> getRejectedFriends(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientService.getRejectedFriends(id));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @GetMapping("/{id}/friend/pending")
    public ResponseEntity<ClientListDTO> getPendingFriends(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientService.getPendingFriends(id));
    }

    @ValidateRoles({"client"})
    @ValidateClient(idField = "requesterId")
    @PostMapping("/{id}/friend/pending/{requesterId}")
    public ResponseEntity<FriendDTO> friendRequest(
            @PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId) throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.friendRequest(requesterId, id));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @DeleteMapping("/{id}/friend/pending/{requesterId}")
    public void deleteFriendRequest(@PathVariable("id") Long id, @PathVariable("requesterId") Long requesterId)
            throws NoContentException {
        clientService.deleteFriendRequest(requesterId, id);
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @PutMapping("/{id}/friend/pending/{requesterId}/accept")
    public ResponseEntity<FriendDTO> acceptFriendRequest(@PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.acceptFriendRequest(requesterId, id));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @PutMapping("/{id}/friend/pending/{requesterId}/reject")
    public ResponseEntity<FriendDTO> rejectFriendRequest(@PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.rejectFriendRequest(requesterId, id));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @GetMapping("/{id}/reservation")
    public ResponseEntity<ReservationListDTO> getReservations(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getReservations(id));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @GetMapping("/{id}/reservation/{date}")
    public ResponseEntity<ReservationListDTO> getReservationsByDate(
            @PathVariable("id") Long id,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date)
            throws NoContentException {
        return ResponseEntity.ok(clientService.getReservationsByDate(id, date));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @GetMapping("/{id}/favorite-branchs")
    public ResponseEntity<BranchListDTO> getFavoriteBranchs(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(clientService.getFavoriteBranchs(id));
    }
    
    @ValidateClient
    @ValidateRoles({"client"})
    @PostMapping("/{id}/favorite-branchs/{branchId}")
    public ResponseEntity<BranchDTO> addFavoriteBranchs(
            @PathVariable("id") Long id,
            @PathVariable("branchId") Long branchId)
            throws NoContentException {
        return ResponseEntity.ok(clientService.addFavoriteBranch(id, branchId));
    }

    @ValidateClient
    @ValidateRoles({"client"})
    @DeleteMapping("/{id}/favorite-branchs/{branchId}")
    public void deleteFavoriteBranch(
            @PathVariable("id") Long id,
            @PathVariable("branchId") Long branchId)
            throws NoContentException {
        clientService.deleteFavoriteBranch(id, branchId);
    }
}


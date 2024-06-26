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
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.client.utils.ValidateClientInterceptor.ValidateClient;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Date;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ClientStatics.Endpoint.PATH)
@Tag(name = "06. Client", description = "Client Management Controller")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @ValidateRoles({})
    @Operation(summary = "Get all clients", description = "Returns a list with all clients")
    public ResponseEntity<ClientListDTO> getAll() {
        return ResponseEntity.ok(clientService.getAll());
    }

    @PostMapping
    @ValidateRoles({ "client" })
    @Operation(summary = "Create new client", description = "Register a new client in the app")
    public ResponseEntity<ClientDTO> save(@RequestBody ClientDTO client)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.save(client));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Gets the data of a client given its ID")
    public ResponseEntity<ClientDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @ValidateClient
    @PutMapping("/{id}")
    @ValidateRoles({ "client" })
    @Operation(summary = "Update client", description = "Updates the data of a client given its ID")
    public ResponseEntity<ClientDTO> update(@PathVariable("id") Long id, @RequestBody ClientDTO client)
            throws NoContentException {
        return ResponseEntity.ok(clientService.update(id, client));
    }

    @ValidateClient
    @DeleteMapping("/{id}")
    @ValidateRoles({ "client" })
    @Operation(summary = "Delete client", description = "Delete the data of a client given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        clientService.delete(id);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get client by user ID", description = "Gets the data of a client given its user ID")
    public ResponseEntity<ClientDTO> getByUserId(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getByUserId(id));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @GetMapping("/{id}/friend/accepted")
    @Operation(summary = "Gets all the friends of the client", description = "Gets the data of the customers who have accepted or were accepted as friends of the current customer given their ID")
    public ResponseEntity<ClientListDTO> getAcceptedFriends(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getAcceptedFriends(id));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @GetMapping("/{id}/friend/rejected")
    @Operation(summary = "Gets all rejected friend requests", description = "Gets the data of customers whose friend requests were rejected by the current customer given their ID")
    public ResponseEntity<ClientListDTO> getRejectedFriends(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getRejectedFriends(id));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @GetMapping("/{id}/friend/pending")
    @Operation(summary = "Gets all pending friend requests", description = "Gets the data of customers whose friend requests have not yet been answered by the current customer given their ID")
    public ResponseEntity<ClientListDTO> getPendingFriends(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getPendingFriends(id));
    }

    @ValidateRoles({ "client" })
    @ValidateClient(idField = "requesterId")
    @PostMapping("/{id}/friend/pending/{requesterId}")
    @Operation(summary = "Send a new friend request", description = "Send a new friend request given the requester and addresser IDs")
    public ResponseEntity<FriendDTO> friendRequest(
            @PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId) throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.friendRequest(requesterId, id));
    }

    @ValidateRoles({ "client" })
    @ValidateClient(idField = "requesterId")
    @DeleteMapping("/{id}/friend/pending/{requesterId}")
    @Operation(summary = "Delete friend request", description = "Delete friend request given its ID")
    public void deleteFriendRequest(@PathVariable("id") Long id, @PathVariable("requesterId") Long requesterId)
            throws NoContentException {
        clientService.deleteFriendRequest(requesterId, id);
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @PutMapping("/{id}/friend/pending/{requesterId}/accept")
    @Operation(summary = "Accept a friend request", description = "Accept friend request given its ID")
    public ResponseEntity<FriendDTO> acceptFriendRequest(@PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.acceptFriendRequest(requesterId, id));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @PutMapping("/{id}/friend/pending/{requesterId}/reject")
    @Operation(summary = "Reject a friend request", description = "Reject friend request given its ID")
    public ResponseEntity<FriendDTO> rejectFriendRequest(@PathVariable("id") Long id,
            @PathVariable("requesterId") Long requesterId)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.rejectFriendRequest(requesterId, id));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @GetMapping("/{id}/reservation")
    @Operation(summary = "Gets all client reservations", description = "Obtains the data of all the reservations of the client given his ID")
    public ResponseEntity<ReservationListDTO> getReservations(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(clientService.getReservations(id));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @GetMapping("/{id}/reservation/{date}")
    @Operation(summary = "Gets all client reservations created after a specific date", description = "Obtains a list with the data of all the reservations created after a specific date of a client given its ID")
    public ResponseEntity<ReservationListDTO> getReservationsByDate(
            @PathVariable("id") Long id,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date)
            throws NoContentException {
        return ResponseEntity.ok(clientService.getReservationsByDate(id, date));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @GetMapping("/{id}/favorite-branches")
    @Operation(summary = "Gets the client's favorite branches", description = "Gets the data of the branches marked as favorites by the client given its ID")
    public ResponseEntity<BranchListDTO> getFavoriteBranches(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(clientService.getFavoriteBranches(id));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @PostMapping("/{id}/favorite-branches/{branchId}")
    @Operation(summary = "Mark a branch as a customer favorite", description = "Mark a branch as a customer favorite given their IDs")
    public ResponseEntity<BranchDTO> addFavoriteBranches(
            @PathVariable("id") Long id,
            @PathVariable("branchId") Long branchId)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(clientService.addFavoriteBranch(id, branchId));
    }

    @ValidateClient
    @ValidateRoles({ "client" })
    @DeleteMapping("/{id}/favorite-branches/{branchId}")
    @Operation(summary = "Unmark a branch as a customer favorite", description = "Unmark a branch as a customer favorite given their IDs")
    public void deleteFavoriteBranch(
            @PathVariable("id") Long id,
            @PathVariable("branchId") Long branchId)
            throws NoContentException {
        clientService.deleteFavoriteBranch(id, branchId);
    }
}

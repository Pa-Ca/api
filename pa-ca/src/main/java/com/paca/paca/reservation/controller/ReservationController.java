package com.paca.paca.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.reservation.dto.ReservationPaymentDTO;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.reservation.utils.ValidateReservationOwnerInterceptor.ValidateReservationOwner;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ReservationStatics.Endpoint.PATH)
@Tag(name = "07. Reservation", description = "Reservation Management Controller")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @ValidateRoles({})
    @Operation(summary = "Get all reservations", description = "Returns a list with all reservations")
    public ResponseEntity<ReservationListDTO> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID", description = "Gets the data of a reservation given its ID")
    public ResponseEntity<ReservationDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create new reservation", description = "Create a new reservation in the app")
    public ResponseEntity<ReservationDTO> save(@RequestBody ReservationDTO dto) throws NoContentException, BadRequestException {
        return ResponseEntity.ok(reservationService.save(dto));
    }

    @ValidateRoles({ "client" })
    @PostMapping("/cancel/{id}")
    @Operation(summary = "Cancel a reservation", description = "Cancel a reservation given your id")
    @ValidateReservationOwner(isClientOwner = true)
    public void cancel(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        reservationService.cancel(id);
    }

    @PostMapping("/reject/{id}")
    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @Operation(summary = "Reject a reservation", description = "Reject a reservation given your id")
    public void reject(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        reservationService.reject(id);
    }

    @PostMapping("/accept/{id}")
    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @Operation(summary = "Accept a reservation", description = "Accept a reservation given your id")
    public void accept(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        reservationService.accept(id);
    }

    @PostMapping("/retire/{id}")
    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @Operation(summary = "Retire a reservation", description = "Retire a reservation given your id")
    public void retire(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        reservationService.retire(id);
    }

    @PostMapping("/start/{id}")
    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @Operation(summary = "Start a reservation", description = "Start a reservation given your id")
    public void start(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        reservationService.start(id);
    }

    @PostMapping("/close/{id}")
    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @Operation(summary = "Close a reservation", description = "Close a reservation given your id")
    public void close(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        reservationService.close(id);
    }

    @PostMapping("/pay/{id}")
    @ValidateRoles({ "client" })
    @ValidateReservationOwner(isClientOwner = true)
    @Operation(summary = "Pay a reservation", description = "Send the payment details of a reservation")
    public void pay(@PathVariable("id") Long id, @RequestBody ReservationPaymentDTO dto)
            throws ForbiddenException, NoContentException, BadRequestException {
        // reservationService.pay(id);
    }

    @PutMapping("/{id}")
    @ValidateRoles({})
    @ValidateReservationOwner(isClientOwner = true)
    @Operation(summary = "Update reservation", description = "Updates the data of a reservation given its ID")
    public ResponseEntity<ReservationDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ReservationDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    @Operation(summary = "Delete reservation", description = "Delete the data of a reservation given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        reservationService.delete(id);
    }
}
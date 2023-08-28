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

import com.paca.paca.reservation.dto.InvoiceDTO;
import com.paca.paca.reservation.dto.ReservationDTO;
import com.paca.paca.reservation.dto.ReservationInfoDTO;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NotFoundException;
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

    @GetMapping(ReservationStatics.Endpoint.GET_BY_ID)
    @Operation(summary = "Get reservation by ID", description = "Gets the data of a reservation given its ID")
    public ResponseEntity<ReservationInfoDTO> getById(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping(ReservationStatics.Endpoint.SAVE)
    @Operation(summary = "Create new reservation", description = "Create a new reservation in the app")
    public ResponseEntity<ReservationInfoDTO> save(@RequestBody ReservationInfoDTO dto)
            throws NotFoundException, BadRequestException {
        return ResponseEntity.ok(reservationService.save(dto));
    }

    @ValidateRoles({ "client" })
    @ValidateReservationOwner(isClientOwner = true)
    @PostMapping(ReservationStatics.Endpoint.CANCEL)
    @Operation(summary = "Cancel a reservation", description = "Cancel a reservation given your id")
    public void cancel(@PathVariable("id") Long id) throws ForbiddenException, NotFoundException, BadRequestException {
        reservationService.cancel(id);
    }

    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @PostMapping(ReservationStatics.Endpoint.REJECT)
    @Operation(summary = "Reject a reservation", description = "Reject a reservation given your id")
    public void reject(@PathVariable("id") Long id) throws ForbiddenException, NotFoundException, BadRequestException {
        reservationService.reject(id);
    }

    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @PostMapping(ReservationStatics.Endpoint.ACCEPT)
    @Operation(summary = "Accept a reservation", description = "Accept a reservation given your id")
    public void accept(@PathVariable("id") Long id) throws ForbiddenException, NotFoundException, BadRequestException {
        reservationService.accept(id);
    }

    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @PostMapping(ReservationStatics.Endpoint.RETIRE)
    @Operation(summary = "Retire a reservation", description = "Retire a reservation given your id")
    public void retire(@PathVariable("id") Long id) throws ForbiddenException, NotFoundException, BadRequestException {
        reservationService.retire(id);
    }

    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @PostMapping(ReservationStatics.Endpoint.START)
    @Operation(summary = "Start a reservation", description = "Start a reservation given your id")
    public void start(@PathVariable("id") Long id) throws ForbiddenException, NotFoundException, BadRequestException {
        reservationService.start(id);
    }

    @ValidateRoles({ "business" })
    @ValidateReservationOwner(isClientOwner = false)
    @PostMapping(ReservationStatics.Endpoint.CLOSE)
    @Operation(summary = "Close a reservation", description = "Close a reservation given your id")
    public void close(@PathVariable("id") Long id) throws ForbiddenException, NotFoundException, BadRequestException {
        reservationService.close(id);
    }

    @ValidateRoles({ "client" })
    @ValidateReservationOwner(isClientOwner = true)
    @PostMapping(ReservationStatics.Endpoint.PAY)
    @Operation(summary = "Pay a reservation", description = "Send the payment details of a reservation")
    public void pay(@PathVariable("id") Long id, @RequestBody InvoiceDTO dto)
            throws ForbiddenException, NotFoundException, BadRequestException {
        reservationService.pay(id, dto);
    }

    @ValidateRoles({})
    @ValidateReservationOwner(isClientOwner = true)
    @PutMapping(ReservationStatics.Endpoint.UPDATE)
    @Operation(summary = "Update reservation", description = "Updates the data of a reservation given its ID")
    public ResponseEntity<ReservationInfoDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ReservationDTO dto)
            throws NotFoundException {
        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    @ValidateRoles({ "business" })
    @DeleteMapping(ReservationStatics.Endpoint.DELETE)
    @Operation(summary = "Delete reservation", description = "Delete the data of a reservation given its ID")
    public void delete(@PathVariable("id") Long id) throws NotFoundException {
        reservationService.delete(id);
    }
}
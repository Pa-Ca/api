package com.paca.paca.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

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

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(ReservationStatics.Endpoint.PATH)
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<ReservationListDTO> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @PostMapping
    @ValidateRoles({"client", "business"})
    public ResponseEntity<ReservationDTO> save(@RequestBody ReservationDTO dto) throws NoContentException {
        return ResponseEntity.ok(reservationService.save(dto));
    }

    @ValidateRoles({"client"})
    @PostMapping("/cancel/{id}")
    @ValidateReservationOwner( isClientOwner = true )
    public void cancel(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        reservationService.accept(id, auth.getName());
    }

    @PostMapping("/accept/{id}")
    @ValidateRoles({ "business" })
    @ValidateReservationOwner( isClientOwner = false )
    public void accept(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        reservationService.accept(id, auth.getName());
    }

    @PostMapping("/reject/{id}")
    @ValidateRoles({ "business" })
    @ValidateReservationOwner( isClientOwner = false )
    public void reject(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        reservationService.accept(id, auth.getName());
    }

    @PostMapping("/pay/{id}")
    @ValidateRoles({ "client" })
    @ValidateReservationOwner( isClientOwner = true )
    public void pay(@PathVariable("id") Long id, @RequestBody ReservationPaymentDTO dto)
            throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        reservationService.accept(id, auth.getName());
    }

    @PutMapping("/{id}")
    @ValidateRoles({ })
    @ValidateReservationOwner( isClientOwner = true )
    public ResponseEntity<ReservationDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ReservationDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(reservationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles({"business"})
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        reservationService.delete(id);
    }
}
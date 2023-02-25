package com.paca.paca.reservation.controller;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.reservation.dto.ReservationPaymentDTO;
import com.paca.paca.statics.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.reservation.statics.ReservationStatics;

import lombok.RequiredArgsConstructor;

import com.paca.paca.exception.exceptions.NoContentException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(ReservationStatics.Endpoint.PATH)
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<ReservationListDTO> getAll() {
        return reservationService.getAll();
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> save(@RequestBody ReservationDTO dto) throws NoContentException {
        return reservationService.save(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return reservationService.getById(id);
    }

    @PostMapping("/cancel/{id}")
    public void cancel(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(UserRole.business.name()))) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }
        reservationService.accept(id, auth.getName());
    }

    @PostMapping("/accept/{id}")
    public void accept(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(UserRole.client.name()))) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }
        reservationService.accept(id, auth.getName());
    }

    @PostMapping("/reject/{id}")
    public void reject(@PathVariable("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println(auth.getAuthorities().stream().findFirst().get());
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(UserRole.client.name()))) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }
        reservationService.accept(id, auth.getName());
    }

    @PostMapping("/pay/{id}")
    public void pay(@PathVariable("id") Long id, @RequestBody ReservationPaymentDTO dto)
            throws ForbiddenException, NoContentException, BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(UserRole.business.name()))) {
            throw new ForbiddenException("Unauthorized access for this operation");
        }
        reservationService.accept(id, auth.getName());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ReservationDTO dto)
            throws NoContentException {
        return reservationService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        reservationService.delete(id);
    }
}
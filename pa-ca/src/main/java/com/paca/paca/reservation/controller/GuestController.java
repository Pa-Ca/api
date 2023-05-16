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

import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.dto.GuestListDTO;
import com.paca.paca.reservation.service.GuestService;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ReservationStatics.Endpoint.GUEST_PATH)
@Tag(name = "07. Guest", description = "Guest Management Controller")
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<GuestListDTO> getAll() {
        return ResponseEntity.ok(guestService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(guestService.getById(id));
    }

    @PostMapping
    @ValidateRoles({})
    public ResponseEntity<GuestDTO> save(@RequestBody GuestDTO dto) {
        return ResponseEntity.ok(guestService.save(dto));
    }

    @PutMapping("/{id}")
    @ValidateRoles({})
    public ResponseEntity<GuestDTO> update(
            @PathVariable("id") Long id,
            @RequestBody GuestDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(guestService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles({})
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        guestService.delete(id);
    }
}

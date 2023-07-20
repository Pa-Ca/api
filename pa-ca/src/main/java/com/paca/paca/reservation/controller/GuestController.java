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

import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.reservation.dto.GuestListDTO;
import com.paca.paca.reservation.service.GuestService;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.statics.ReservationStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.business.model.Business;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ReservationStatics.Endpoint.GUEST_PATH)
@Tag(name = "08. Guest", description = "Guest Management Controller")
public class GuestController {

    private final GuestService guestService;
    private final BusinessRepository businessRepository;

    @GetMapping
    @ValidateRoles({})
    @Operation(summary = "Get all user guest", description = "Returns a list with all user guest")
    public ResponseEntity<GuestListDTO> getAll() {
        return ResponseEntity.ok(guestService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user guest by ID", description = "Gets the data of a user guest given its ID")
    public ResponseEntity<GuestDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(guestService.getById(id));
    }

    @GetMapping("/identity-document/{identityDocument}")
    @ValidateRoles({ "business" })
    @Operation(summary = "Get user guest by identity document", description = "Gets the data of a user guest given its identity document")
    public ResponseEntity<GuestDTO> getByIdentityDocument(@PathVariable("identityDocument") String identityDocument) throws NoContentException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Business business = businessRepository.findByUserEmail(auth.getName()).get();
        return ResponseEntity.ok(guestService.getByIdentityDocument(business.getId(),identityDocument));
    }

    @PostMapping
    @ValidateRoles({})
    @Operation(summary = "Create new user guest", description = "Create a new user guest in the app")
    public ResponseEntity<GuestDTO> save(@RequestBody GuestDTO dto) {
        return ResponseEntity.ok(guestService.save(dto));
    }

    @ValidateRoles({})
    @PutMapping("/{id}")
    @Operation(summary = "Update user guest", description = "Updates the data of a user guest given its ID")
    public ResponseEntity<GuestDTO> update(
            @PathVariable("id") Long id,
            @RequestBody GuestDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(guestService.update(id, dto));
    }

    @ValidateRoles({})
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user guest", description = "Delete the data of a user guest given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        guestService.delete(id);
    }
}

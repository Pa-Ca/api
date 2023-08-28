package com.paca.paca.client.controller;

import com.paca.paca.client.dto.ReviewDTO;
import com.paca.paca.client.service.ReviewService;
import com.paca.paca.client.statics.ReviewStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.client.utils.ValidateClientInterceptor.ValidateClient;
import com.paca.paca.client.utils.ValidateReviewOwnerInterceptor.ValidateReviewOwner;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(ReviewStatics.Endpoint.PATH)
@Tag(name = "13. Review", description = "Review Management Controller")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(ReviewStatics.Endpoint.SAVE)
    @ValidateRoles({ "client" })
    @Operation(summary = "Create new review", description = "Create a new review in the app")
    public ResponseEntity<ReviewDTO> save(@RequestBody ReviewDTO dto)
            throws NotFoundException, ConflictException {
        return ResponseEntity.ok(reviewService.save(dto));
    }

    @GetMapping(ReviewStatics.Endpoint.GET_BY_ID)
    @Operation(summary = "Get review by ID", description = "Gets the data of a review given its ID")
    public ResponseEntity<ReviewDTO> getById(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @ValidateReviewOwner
    @ValidateRoles({ "client" })
    @PutMapping(ReviewStatics.Endpoint.UPDATE)
    @Operation(summary = "Update review", description = "Updates the data of a review given its ID")
    public ResponseEntity<ReviewDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ReviewDTO dto)
            throws NotFoundException {
        return ResponseEntity.ok(reviewService.update(id, dto));
    }

    @ValidateReviewOwner
    @ValidateRoles({ "client" })
    @DeleteMapping(ReviewStatics.Endpoint.DELETE)
    @Operation(summary = "Update review", description = "Updates the data of a review given its ID")
    public void delete(@PathVariable("id") Long id) throws NotFoundException {
        reviewService.delete(id);
    }

    @ValidateRoles({ "client" })
    @ValidateClient(idField = "clientId")
    @PutMapping(ReviewStatics.Endpoint.LIKE)
    @Operation(summary = "Mark a review as a customer favorite", description = "Mark a review as a customer's favorite given their IDs")
    public ResponseEntity<ReviewDTO> like(
            @PathVariable("id") Long id,
            @PathVariable("clientId") Long clientId) throws NotFoundException, ConflictException {
        return ResponseEntity.ok(reviewService.like(id, clientId));
    }

    @ValidateRoles({ "client" })
    @ValidateClient(idField = "clientId")
    @DeleteMapping(ReviewStatics.Endpoint.DISLIKE)
    @Operation(summary = "Unmark a review as a customer favorite", description = "Unmark a review as a customer's favorite given their IDs")
    public ResponseEntity<ReviewDTO> dislike(
            @PathVariable("id") Long id,
            @PathVariable("clientId") Long clientId) throws NotFoundException {
        return ResponseEntity.ok(reviewService.dislike(id, clientId));
    }

}

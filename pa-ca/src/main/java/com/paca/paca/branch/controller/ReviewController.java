package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.ReviewDTO;
import com.paca.paca.branch.dto.ReviewListDTO;
import com.paca.paca.branch.statics.ReviewStatics;
import com.paca.paca.branch.service.ReviewService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(ReviewStatics.Endpoint.PATH)
public class ReviewController {
    
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ReviewListDTO> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> save(@RequestBody ReviewDTO dto) throws NoContentException {
        return ResponseEntity.ok(reviewService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> update(
            @PathVariable("id") Long id,
            @RequestBody ReviewDTO dto)
            throws NoContentException {
        return ResponseEntity.ok(reviewService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        reviewService.delete(id);
    }

    @PutMapping("/{id}/client/{clientId}")
    public ResponseEntity<ReviewDTO> like(
            @PathVariable("id") Long id,
            @PathVariable("clientId") Long clientId) throws NoContentException, ConflictException {
        return ResponseEntity.ok(reviewService.like(id, clientId));
    }

    @DeleteMapping("/{id}/client/{clientId}")
    public ResponseEntity<ReviewDTO> dislike(
            @PathVariable("id") Long id,
            @PathVariable("clientId") Long clientId) throws NoContentException {
        return ResponseEntity.ok(reviewService.dislike(id, clientId));
    }
}

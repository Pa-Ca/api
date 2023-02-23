package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;

import lombok.RequiredArgsConstructor;

import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(BranchStatics.Endpoint.PATH)
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    public ResponseEntity<BranchListDTO> getAll() {
        return ResponseEntity.ok(branchService.getAll());
    }

    @PostMapping
    public ResponseEntity<BranchDTO> save(@RequestBody BranchDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(branchService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> update(
            @PathVariable("id") Long id,
            @RequestBody BranchDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(branchService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        branchService.delete(id);
    }

    @GetMapping("/{id}/product-category/{productCategoryId}")
    public ResponseEntity<ProductSubCategoryListDTO> getProductSubCategories(
            @PathVariable("id") Long id,
            @PathVariable("productCategoryId") Long productCategoryId) throws NoContentException {
        return ResponseEntity.ok(branchService.getProductSubCategories(id, productCategoryId));
    }

    @GetMapping("/{id}/product")
    public ResponseEntity<ProductListDTO> getProducts(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(branchService.getProducts(id));
    }

    @GetMapping("/{id}/promotion")
    public ResponseEntity<PromotionListDTO> getPromotions(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(branchService.getPromotions(id));
    }

    @GetMapping("/{id}/reservation")
    public ResponseEntity<ReservationListDTO> getReservations(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(branchService.getReservations(id));
    }

    @GetMapping("/{id}/reservation/{date}")
    public ResponseEntity<ReservationListDTO> getReservationsByDate(
            @PathVariable("id") Long id,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getReservationsByDate(id, date));
    }

    @GetMapping("/{id}/favorite-clients")
    public ResponseEntity<ClientListDTO> getFavoriteClients(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getFavoriteClients(id));
    }
}

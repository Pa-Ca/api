package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.reservation.dto.ReservationListDTO;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;
import com.paca.paca.branch.utils.ValidateBranchOwnerInterceptor.ValidateBranchOwner;

import java.util.Date;
import lombok.RequiredArgsConstructor;

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
import com.paca.paca.exception.exceptions.UnprocessableException;

// Import the module to use RequestParam
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(BranchStatics.Endpoint.PATH)
public class BranchController {

    private final BranchService branchService;

    private final AmenityService amenityService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<BranchListDTO> getAll() {
        return ResponseEntity.ok(branchService.getAll());
    }

    @PostMapping
    @ValidateRoles({"business"})
    public ResponseEntity<BranchDTO> save(@RequestBody BranchDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(branchService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @PutMapping("/{id}")
    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    public ResponseEntity<BranchDTO> update(
            @PathVariable("id") Long id,
            @RequestBody BranchDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(branchService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ValidateBranchOwner
    @ValidateRoles({"business"})
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
    public ResponseEntity<ProductListDTO> getProducts(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getProducts(id));
    }

    @GetMapping("/{id}/promotion")
    public ResponseEntity<PromotionListDTO> getPromotions(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getPromotions(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @GetMapping("/{id}/reservation")
    public ResponseEntity<ReservationListDTO> getReservations(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getReservations(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @GetMapping("/{id}/reservation/{date}")
    public ResponseEntity<ReservationListDTO> getReservationsByDate(
            @PathVariable("id") Long id,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getReservationsByDate(id, date));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @GetMapping("/{id}/favorite-clients")
    public ResponseEntity<ClientListDTO> getFavoriteClients(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getFavoriteClients(id));
    }

    @GetMapping("/{id}/review")
    public ResponseEntity<ReviewListDTO> getReviews(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getReviews(id));
    }

    @GetMapping("/{id}/amenity")
    public ResponseEntity<AmenityListDTO> getAllByBranchId(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(amenityService.getAllByBranchId(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @PostMapping("/{id}/amenity")
    public ResponseEntity<AmenityListDTO> saveBranchAmenities(
            @PathVariable("id") Long id, @RequestBody AmenityListDTO dto) throws NoContentException {
        return ResponseEntity.ok(amenityService.saveAllByBranchId(id, dto));
    }

    @ValidateBranchOwner
    @ValidateRoles({"business"})
    @DeleteMapping("/{id}/amenity")
    public ResponseEntity<AmenityListDTO> deleteAllByBranchId(
            @PathVariable("id") Long id,
            @RequestBody AmenityListDTO dto) throws NoContentException {
        return ResponseEntity.ok(amenityService.deleteAllByBranchId(id, dto));
    }

    // Example get http://yourdomain.com/1/reviews?page=2&size=5
    @GetMapping("/{id}/reviews")
    public ResponseEntity<ReviewListDTO> getReviewPage(
            @PathVariable("id") Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size 
            ) throws NoContentException, UnprocessableException {
        return ResponseEntity.ok(branchService.getReviewPage(id, page, size));
    }

}

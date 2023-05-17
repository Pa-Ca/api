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
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.product_sub_category.dto.ProductSubCategoryListDTO;
import com.paca.paca.branch.utils.ValidateBranchOwnerInterceptor.ValidateBranchOwner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(BranchStatics.Endpoint.PATH)
@Tag(name = "05. Branch", description = "Branch Management Controller")
public class BranchController {

    private final BranchService branchService;

    private final AmenityService amenityService;

    @GetMapping
    @ValidateRoles({})
    @Operation(summary = "Get all branches", description = "Returns a list with all branches")
    public ResponseEntity<BranchListDTO> getAll() {
        return ResponseEntity.ok(branchService.getAll());
    }

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new branch", description = "Create a new branch in the app")
    public ResponseEntity<BranchDTO> save(@RequestBody BranchDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(branchService.save(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get branch by ID", description = "Gets the data of a branch given its ID")
    public ResponseEntity<BranchDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @PutMapping("/{id}")
    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @Operation(summary = "Update branch", description = "Updates the data of a branch given its ID")
    public ResponseEntity<BranchDTO> update(
            @PathVariable("id") Long id,
            @RequestBody BranchDTO dto)
            throws NoContentException, BadRequestException {
        return ResponseEntity.ok(branchService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @Operation(summary = "Delete branch", description = "Delete the data of a branch given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        branchService.delete(id);
    }

    @GetMapping("/{id}/product-category/{productCategoryId}")
    @Operation(summary = "Get all product sub-categories of a branch", description = "Gets a list with the data of all the product sub-categories of a branch given its id")
    public ResponseEntity<ProductSubCategoryListDTO> getProductSubCategories(
            @PathVariable("id") Long id,
            @PathVariable("productCategoryId") Long productCategoryId) throws NoContentException {
        return ResponseEntity.ok(branchService.getProductSubCategories(id, productCategoryId));
    }

    @GetMapping("/{id}/product")
    @Operation(summary = "Get all products of a branch", description = "Gets a list with the data of all the products of a branch given its id")
    public ResponseEntity<ProductListDTO> getProducts(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getProducts(id));
    }

    @GetMapping("/{id}/promotion")
    @Operation(summary = "Get all promotions of a branch", description = "Gets a list with the data of all the promotions of a branch given its id")
    public ResponseEntity<PromotionListDTO> getPromotions(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getPromotions(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @GetMapping("/{id}/reservation")
    @Operation(summary = "Get all reservations of a branch", description = "Gets a list with the data of all the reservations of a branch given its id")
    public ResponseEntity<ReservationListDTO> getReservations(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getReservations(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @GetMapping("/{id}/reservation/{date}")
    @Operation(summary = "Get all reservations created after a specific date of a branch", description = "Obtains a list with the data of all the reservations created after a specific date of a branch given its id")
    public ResponseEntity<ReservationListDTO> getReservationsByDate(
            @PathVariable("id") Long id,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getReservationsByDate(id, date));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @GetMapping("/{id}/favorite-clients")
    @Operation(summary = "Gets all clients that have bookmarked this branch", description = "Gets a list with the data of all the users who have marked the branch as favorites given its id")
    public ResponseEntity<ClientListDTO> getFavoriteClients(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getFavoriteClients(id));
    }

    @GetMapping("/{id}/review")
    @Operation(summary = "Get all reviews of a branch", description = "Gets a list with the data of all the reviews of a branch given its id")
    public ResponseEntity<ReviewListDTO> getReviews(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(branchService.getReviews(id));
    }

    @GetMapping("/{id}/amenity")
    @Operation(summary = "Get all amenities of a branch", description = "Gets a list with the data of all the amenities of a branch given its id")
    public ResponseEntity<AmenityListDTO> getAllAmenititesByBranchId(@PathVariable("id") Long id)
            throws NoContentException {
        return ResponseEntity.ok(amenityService.getAllByBranchId(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @PostMapping("/{id}/amenity")
    @Operation(summary = "Associate a list of amenities to the branch", description = "Associates a list of amenities to the branch given its id. Amenities already associated or that do not exist will be ignored")
    public ResponseEntity<AmenityListDTO> saveBranchAmenities(
            @PathVariable("id") Long id, @RequestBody AmenityListDTO dto) throws NoContentException {
        return ResponseEntity.ok(amenityService.saveAllByBranchId(id, dto));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @DeleteMapping("/{id}/amenity")
    @Operation(summary = "Detach a list of amenities to the branch", description = "Detach a list of amenities from the branch given its id. Amenities that were not associated or do not exist will be ignored")
    public ResponseEntity<AmenityListDTO> deleteAllByBranchId(
            @PathVariable("id") Long id,
            @RequestBody AmenityListDTO dto) throws NoContentException {
        return ResponseEntity.ok(amenityService.deleteAllByBranchId(id, dto));
    }

    // Example get http://yourdomain.com/1/reviews?page=2&size=5
    @GetMapping("/{id}/reviews")
    @Operation(summary = "Gets a branch reviews page", description = "Gets a page with the data of the reviews of the branch given its id")
    public ResponseEntity<ReviewListDTO> getReviewsPage(
            @PathVariable("id") Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size) throws NoContentException, UnprocessableException {
        return ResponseEntity.ok(branchService.getReviewsPage(id, page, size));
    }

    @GetMapping("/branches")
    @Operation(summary = "Gets a page of branches", description = "Gets a page with the data of branches")
    public ResponseEntity<BranchListDTO> getBranchesPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sorting_by") String sorting_by,
            @RequestParam("ascending") boolean ascending,
            @RequestParam("min_reservation_price") Float min_reservation_price,
            @RequestParam("max_reservation_price") Float max_reservation_price,
            @RequestParam("min_score") Float min_score,
            @RequestParam("min_capacity") int min_capacity) throws NoContentException, UnprocessableException {
        return ResponseEntity.ok(branchService.getBranchesPage(
                page,
                size,
                sorting_by,
                ascending,
                min_reservation_price,
                max_reservation_price,
                min_score,
                min_capacity));
    }

}

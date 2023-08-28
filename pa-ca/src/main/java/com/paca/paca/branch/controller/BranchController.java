package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.dto.TableListDTO;
import com.paca.paca.client.dto.ClientListDTO;
import com.paca.paca.client.dto.ReviewListDTO;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.product.dto.ProductListDTO;
import com.paca.paca.sale.dto.BranchSalesInfoDTO;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.statics.BranchStatics;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.branch.dto.PaymentOptionListDTO;
import com.paca.paca.reservation.service.ReservationService;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.reservation.dto.BranchReservationsInfoDTO;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.productSubCategory.dto.ProductSubCategoryListDTO;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
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
import java.util.List;

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

    private final SaleService saleService;

    private final BranchService branchService;

    private final AmenityService amenityService;

    private final ReservationService reservationService;

    @ValidateRoles({ "business" })
    @PostMapping(BranchStatics.Endpoint.SAVE)
    @Operation(summary = "Create new branch", description = "Create a new branch in the app")
    public ResponseEntity<BranchDTO> save(@RequestBody BranchDTO dto)
            throws NotFoundException, BadRequestException {
        return ResponseEntity.ok(branchService.save(dto));
    }

    @GetMapping(BranchStatics.Endpoint.GET_BY_ID)
    @Operation(summary = "Get branch by ID", description = "Gets the data of a branch given its ID")
    public ResponseEntity<BranchDTO> getById(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(branchService.getById(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @PutMapping(BranchStatics.Endpoint.UPDATE)
    @Operation(summary = "Update branch", description = "Updates the data of a branch given its ID")
    public ResponseEntity<BranchDTO> update(
            @PathVariable("id") Long id,
            @RequestBody BranchDTO dto)
            throws NotFoundException, BadRequestException {
        return ResponseEntity.ok(branchService.update(id, dto));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(BranchStatics.Endpoint.DELETE)
    @Operation(summary = "Delete branch", description = "Delete the data of a branch given its ID")
    public void delete(@PathVariable("id") Long id) throws NotFoundException {
        branchService.delete(id);
    }

    @GetMapping(BranchStatics.Endpoint.GET_ALL_PRODUCT_SUB_CATEGORIES)
    @Operation(summary = "Get all product sub-categories of a branch", description = "Gets a list with the data of all the product sub-categories of a branch")
    public ResponseEntity<ProductSubCategoryListDTO> getProductSubCategories(
            @PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(branchService.getProductSubCategories(id));
    }

    @GetMapping(BranchStatics.Endpoint.GET_ALL_PRODUCTS)
    @Operation(summary = "Get all products of a branch", description = "Gets a list with the data of all the products of a branch given its id")
    public ResponseEntity<ProductListDTO> getProducts(@PathVariable("id") Long id)
            throws NotFoundException {
        return ResponseEntity.ok(branchService.getProducts(id));
    }

    @GetMapping(BranchStatics.Endpoint.GET_ALL_PROMOTIONS)
    @Operation(summary = "Get all promotions of a branch", description = "Gets a list with the data of all the promotions of a branch given its id")
    public ResponseEntity<PromotionListDTO> getPromotions(@PathVariable("id") Long id)
            throws NotFoundException {
        return ResponseEntity.ok(branchService.getPromotions(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @GetMapping(BranchStatics.Endpoint.GET_ALL_FAVORITE_CLIENTS)
    @Operation(summary = "Gets all clients that have bookmarked this branch", description = "Gets a list with the data of all the users who have marked the branch as favorites given its id")
    public ResponseEntity<ClientListDTO> getFavoriteClients(@PathVariable("id") Long id)
            throws NotFoundException {
        return ResponseEntity.ok(branchService.getFavoriteClients(id));
    }

    @GetMapping(BranchStatics.Endpoint.GET_ALL_AMENITIES)
    @Operation(summary = "Get all amenities of a branch", description = "Gets a list with the data of all the amenities of a branch given its id")
    public ResponseEntity<AmenityListDTO> getAllAmenititesByBranchId(@PathVariable("id") Long id)
            throws NotFoundException {
        return ResponseEntity.ok(amenityService.getAllByBranchId(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @PostMapping(BranchStatics.Endpoint.SAVE_AMENITIES)
    @Operation(summary = "Associate a list of amenities to the branch", description = "Associates a list of amenities to the branch given its id. Amenities already associated or that do not exist will be ignored")
    public ResponseEntity<AmenityListDTO> saveBranchAmenities(
            @PathVariable("id") Long id, @RequestBody AmenityListDTO dto) throws NotFoundException {
        return ResponseEntity.ok(amenityService.saveAllByBranchId(id, dto));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(BranchStatics.Endpoint.DELETE_AMENITIES)
    @Operation(summary = "Detach a list of amenities to the branch", description = "Detach a list of amenities from the branch given its id. Amenities that were not associated or do not exist will be ignored")
    public ResponseEntity<AmenityListDTO> deleteAllByBranchId(
            @PathVariable("id") Long id,
            @RequestBody AmenityListDTO dto) throws NotFoundException {
        return ResponseEntity.ok(amenityService.deleteAllByBranchId(id, dto));
    }

    @GetMapping(BranchStatics.Endpoint.GET_REVIEWS)
    @Operation(summary = "Gets a branch reviews page", description = "Gets a page with the data of the reviews of the branch given its id")
    public ResponseEntity<ReviewListDTO> getReviewsPage(
            @PathVariable("id") Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size) throws NotFoundException, UnprocessableException {
        return ResponseEntity.ok(branchService.getReviewsPage(id, page, size));
    }

    @ValidateRoles({ "business" })
    @GetMapping(BranchStatics.Endpoint.GET_RESERVATIONS)
    public ResponseEntity<BranchReservationsInfoDTO> getBranchReservationsPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "status", required = false) List<Short> status,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endTime,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "identityDocument", required = false) String identitiyDocument,
            @PathVariable("id") Long branchId) throws NotFoundException, UnprocessableException {
        return ResponseEntity.ok(reservationService.getBranchReservations(
                page,
                size,
                branchId,
                status,
                startTime,
                endTime,
                fullname,
                identitiyDocument));
    }

    @ValidateRoles({ "business" })
    @GetMapping(BranchStatics.Endpoint.GET_SALES)
    @Operation(summary = "Gets all the ongoing sales and the historic sales with its taxes and saleporducts", description = "Gets all the ongoing sales and the historic sales; the size and the page are for the historic sales")
    public ResponseEntity<BranchSalesInfoDTO> getBranchSalesPages(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endTime,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "identityDocument", required = false) String identitiyDocument,
            @PathVariable(value = "id", required = false) Long branchId)
            throws NotFoundException, UnprocessableException {
        return ResponseEntity.ok(saleService.getBranchSales(
                page,
                size,
                branchId,
                startTime,
                endTime,
                fullname,
                identitiyDocument));
    }

    @ValidateRoles({ "business" })
    @GetMapping(BranchStatics.Endpoint.GET_ALL_TABLES)
    @Operation(summary = "Gets all the tables of a branch", description = "Gets all the tables of a branch given its id")
    public ResponseEntity<TableListDTO> getTablesByBranchId(@PathVariable("id") Long id)
            throws NotFoundException {
        return ResponseEntity.ok(branchService.getTablesByBranchId(id));
    }

    @ValidateRoles({ "business" })
    @GetMapping(BranchStatics.Endpoint.GET_ALL_PAYMENT_OPTIONS)
    @Operation(summary = "Gets all the payment options of a branch", description = "Gets all the payment options of a branch given its id")
    public ResponseEntity<PaymentOptionListDTO> getPaymentOptionsByBranchId(@PathVariable("id") Long id)
            throws NotFoundException {
        return ResponseEntity.ok(branchService.getPaymentOptionsByBranchId(id));
    }

}

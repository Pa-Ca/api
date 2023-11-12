package com.paca.paca.coupon.controller;

import com.paca.paca.coupon.dto.CouponDTO;
import com.paca.paca.coupon.service.CouponService;
import com.paca.paca.coupon.statics.CouponStatics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.branch.utils.ValidateBranchOwnerInterceptor.ValidateBranchOwner;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(CouponStatics.Endpoint.PATH)
@Tag(name = "0N. Coupon", description = "Coupon Management Controller")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("")
    @Operation(summary = "Get all Coupons", description = "Gets the data of all coupons")
    public ResponseEntity<List<CouponDTO>> getAll() {
        return ResponseEntity.ok(couponService.getAll());
    }

    @GetMapping(CouponStatics.Endpoint.GET_BY_PAGE)
    @Operation(summary = "Get all Coupons", description = "Gets the data of all coupons")
    public ResponseEntity<Page<CouponDTO>> getAllByPage(Pageable pageable) {
        return ResponseEntity.ok(couponService.getAllByPage(pageable));
    }

    @GetMapping(CouponStatics.Endpoint.GET_BY_ID)
    @Operation(summary = "Get coupon by id", description = "Gets the data of a coupons if exists")
    public ResponseEntity<CouponDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getById(id));
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @DeleteMapping(CouponStatics.Endpoint.DELETE)
    @Operation(summary = "Delete coupon", description = "Delete the data of a coupon given its ID")
    public void delete(@PathVariable Long id) {
        couponService.delete(id);
    }

    @ValidateBranchOwner
    @ValidateRoles({ "business" })
    @PostMapping(CouponStatics.Endpoint.SAVE)
    @Operation(summary = "Create coupon", description = "Create a new coupon given a DTO object")
    public ResponseEntity<CouponDTO> save(@PathVariable(required = false) Long branchId, @RequestBody CouponDTO dto) {
        return ResponseEntity.ok(couponService.save(branchId, dto));
    }
}

package com.paca.paca.sale.controller;

import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleListDTO;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;

//import BigDecimal
import java.util.Date;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(SaleStatics.Endpoint.PATH)
@Tag(name = "01. Sale", description = "Sale Management Controller")
public class SaleController {

    private final SaleService saleService;


    @GetMapping("/sales")
    @ValidateRoles({ "business" })
    @Operation(summary = "Gets a page of sales", description = "Gets a page with the data of the sales from an specific branch")
    public ResponseEntity<SaleListDTO> getSalesPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("branch_id") Long branch_id,
            @RequestParam("sorting_by") String sorting_by,
            @RequestParam("ascending") boolean ascending,
            @RequestParam("status") Integer status,
            @RequestParam("end_date") Date end_date,
            @RequestParam("start_date") Date start_date
            ) throws NoContentException, UnprocessableException {
        return ResponseEntity.ok(saleService.getSalesPage(
                 page,
                 size,
                 branch_id,
                 sorting_by,
                 ascending,
                 start_date,
                 end_date,
                 status));
    }

    // Post Methods
    @ValidateRoles({ "business" })
    @PostMapping("/newSale")
    @Operation(summary = "Create new sale", description = "Create a new sale in the app")
    public ResponseEntity<SaleDTO> save(@RequestBody SaleDTO dto) throws NoContentException, BadRequestException {
        return ResponseEntity.ok(saleService.save(dto));
    }


    @ValidateRoles({ "business" })
    @PostMapping("/cancel")
    @Operation(summary = "Cancel a sale", description = "Cancel a sale given its id")
    public void cancel(@RequestParam("id") Long id) throws ForbiddenException, NoContentException, BadRequestException {
        saleService.cancel(id);
    }

}

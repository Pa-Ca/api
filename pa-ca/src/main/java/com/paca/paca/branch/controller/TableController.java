package com.paca.paca.branch.controller;

import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.service.TableService;
import com.paca.paca.branch.statics.TableStatics;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.branch.utils.ValidateTableOwnerInterceptor.ValidateTableOwner;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(TableStatics.Endpoint.PATH)
@Tag(name = "01. Table", description = "Table Controller")
public class TableController {

    private final TableService tableService;

    @PostMapping
    @ValidateRoles({ "business" })
    @Operation(summary = "Create new table", description = "Create a new table")
    public ResponseEntity<TableDTO> save(@RequestBody TableDTO dto)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(tableService.save(dto));
    }

    @PutMapping("/{id}")
    @ValidateRoles({ "business" })
    @ValidateTableOwner
    @Operation(summary = "Update table", description = "Updates the data of a table given its ID and DTO")
    public ResponseEntity<TableDTO> update(
            @PathVariable("id") Long id,
            @RequestBody TableDTO dto)
            throws NoContentException, ConflictException {
        return ResponseEntity.ok(tableService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ValidateRoles({ "business" })
    // @ValidateTableOwner
    @Operation(summary = "Delete table", description = "Delete the data of a table given its ID")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        tableService.delete(id);
    }
}

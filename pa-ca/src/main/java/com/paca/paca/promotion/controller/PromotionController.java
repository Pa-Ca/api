package com.paca.paca.promotion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paca.paca.promotion.dto.PromotionDTO;
import com.paca.paca.promotion.dto.PromotionListDTO;
import com.paca.paca.promotion.service.PromotionService;
import com.paca.paca.promotion.statics.PromotionStatics;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.auth.utils.ValidateRolesInterceptor.ValidateRoles;
import com.paca.paca.promotion.utils.ValidatePromotionOwnerInterceptor.ValidatePromotionOwner;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(PromotionStatics.Endpoint.PATH)
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    @ValidateRoles({})
    public ResponseEntity<PromotionListDTO> getAll() {
        return promotionService.getAll();
    }

    @PostMapping
    @ValidateRoles({"business"})
    public ResponseEntity<PromotionDTO> save(@RequestBody PromotionDTO dto) throws NoContentException {
        return promotionService.save(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getById(@PathVariable("id") Long id) throws NoContentException {
        return promotionService.getById(id);
    }

    @PutMapping("/{id}")
    @ValidatePromotionOwner
    @ValidateRoles({ "business" })
    public ResponseEntity<PromotionDTO> update(
            @PathVariable("id") Long id,
            @RequestBody PromotionDTO dto)
            throws NoContentException {
        return promotionService.update(id, dto);
    }

    @ValidatePromotionOwner
    @DeleteMapping("/{id}")
    @ValidateRoles({"business"})
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        promotionService.delete(id);
    }
}
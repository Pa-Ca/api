package com.paca.paca.analytics;

import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.utils.SaleMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final SaleMapper saleMapper;
    private final SaleRepository saleRepository;

    @GetMapping("/sales")
    public ResponseEntity<List<SaleDTO>> getSalesByTime(@RequestParam String time) {

        List<Sale> sales = saleRepository.findAll(SaleSpecs.salesLastYear());

        return ResponseEntity.ok(sales.stream().map(saleMapper::toDTO).collect(Collectors.toList()));
    }
}

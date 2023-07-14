package com.paca.paca.sale.service;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.dto.BranchSalesInfoDTO;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.UnprocessableException;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    private final SaleMapper saleMapper;

    private final TaxMapper taxMapper;

    private final BranchRepository branchRepository;

    private final TableRepository tableRepository;

    private final TaxRepository taxRepository;

    private final SaleProductRepository saleProductRepository;

    private final SaleProductMapper saleProductMapper;

    private final DefaultTaxRepository defaultTaxRepository;

    public List<TaxDTO> getTaxesBySaleId(Long saleId) throws NoContentException {
        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + saleId + " does not exists",
                    42);
        }

        // Get the taxes
        List<Tax> taxes = taxRepository.findAllBySaleId(saleId);

        // Map the taxes to DTOs
        List<TaxDTO> response = new ArrayList<>();

        for (Tax tax : taxes) {
            TaxDTO dto = taxMapper.toDTO(tax);
            response.add(dto);
        }

        // Return the DTOs
        return response;
    }

    public void delete(Long saleId) throws NoContentException {

        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + saleId + " does not exists",
                    42); // Lista en docs
        }

        saleRepository.deleteById(saleId);
    }

    public List<SaleProductDTO> getSaleProductsbySaleId(long saleId)
            throws NoContentException {

        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (!sale.isPresent()) {
            throw new NoContentException("Sale with id " + saleId + " does not exists", 42);
        }

        // Get the products
        List<SaleProduct> saleProducts = saleProductRepository.findAllBySaleId(saleId);

        // Map the products to DTOs
        List<SaleProductDTO> response = new ArrayList<>();
        for (SaleProduct saleProduct : saleProducts) {

            SaleProductDTO dto = saleProductMapper.toDTO(saleProduct);
            response.add(dto);
        }

        // Return the DTOs
        return response;
    }

    public SaleInfoDTO save(SaleDTO dto) throws NoContentException {

        Optional<Table> table = tableRepository.findById(dto.getTableId());

        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + dto.getTableId() + " does not exists", 49); // !
        }
        Sale newSale_ = saleMapper.toEntity(dto, table.get(), null);
        final Sale newSale = saleRepository.save(newSale_);
        SaleDTO saleDTO = saleMapper.toDTO(newSale);

        // Create taxes
        defaultTaxRepository.findAllByBranchId(table.get().getBranch().getId()).forEach(tax -> {
            Tax newTax = Tax.builder()
                    .sale(newSale)
                    .name(tax.getName())
                    .type(tax.getType())
                    .value(tax.getValue())
                    .build();
            taxRepository.save(newTax);
        });

        List<TaxDTO> taxListDTO = getTaxesBySaleId(newSale.getId());
        List<SaleProductDTO> saleProductListDTO = getSaleProductsbySaleId(newSale.getId());

        // Create a SaleInfo DTO
        SaleInfoDTO dtoResponse = SaleInfoDTO.builder()
                .sale(saleDTO)
                .taxes(taxListDTO)
                .products(saleProductListDTO)
                .build();

        return dtoResponse;
    }

    // Do not use this method (not tested)
    public void cancel(Long id) throws NoContentException, BadRequestException {
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + id + " does not exists",
                    42); // Lista en docs
        }

        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " can not be canceled because it is already closed", 43); // Lista en docs
        }

        Long tableId = sale.get().getTable().getId();
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + tableId + " does not exists",
                    49); // Lista en docs!
        }

        SaleDTO dto = SaleDTO.builder().status(SaleStatics.Status.cancelled).build();
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        saleRepository.save(updatedSale);
    }

    // Do not use this method (not tested)
    public void close(Long id) throws NoContentException, BadRequestException {
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + id + " does not exists",
                    42); // Lista en docs
        }

        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " can not be closed because it is already closed", 43); // Lista en docs
        }

        // add exception (user cannot close a canceled sale)

        if (sale.get().getStatus().equals(SaleStatics.Status.cancelled)) {
            throw new BadRequestException(
                    "Sale with id " + id + " can not be closed because it was canceled", 48);
        }

        Long tableId = sale.get().getTable().getId();
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + tableId + " does not exists", 49); // Lista en docs!
        }

        SaleDTO dto = SaleDTO.builder().status(SaleStatics.Status.closed).build();
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        saleRepository.save(updatedSale);
    }

    public SaleInfoDTO update(long id, SaleDTO dto) throws NoContentException, BadRequestException {

        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + id + " does not exists",
                    42); // Lista en docs
        }

        // Check if the sale is closed
        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is closed", 43); // Lista en docs
        }

        // Check if the sale is canceled
        if (sale.get().getStatus().equals(SaleStatics.Status.cancelled)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is canceled", 48); // Lista en docs
        }

        // Update the sale
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        updatedSale = saleRepository.save(updatedSale);
        SaleDTO updatedSaleDTO = saleMapper.toDTO(updatedSale);

        List<TaxDTO> taxListDTO = getTaxesBySaleId(updatedSale.getId());
        List<SaleProductDTO> saleProductListDTO = getSaleProductsbySaleId(updatedSale.getId());

        // Create a SaleInfo DTO
        SaleInfoDTO dtoResponse = SaleInfoDTO.builder()
                .sale(updatedSaleDTO)
                .taxes(taxListDTO)
                .products(saleProductListDTO)
                .build();

        return dtoResponse;
    }

    public void clearSaleProducts(long saleId) throws NoContentException, BadRequestException {
        // Deletes all the SaleProducts of a Sale given its id

        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + saleId + " does not exists",
                    42); // Lista en docs
        }

        // Check if the sale is closed
        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + saleId + " is closed",
                    43); // No esta referenciado en docs
        }

        // Check if the sale is canceled
        if (sale.get().getStatus().equals(SaleStatics.Status.cancelled)) {
            throw new BadRequestException(
                    "Sale with id " + saleId + " is canceled",
                    48); // No esta referenciado en docs
        }

        // Delete all the SaleProducts of the Sale
        saleProductRepository.deleteAllBySaleId(saleId);
    }

    public void clearSaleTaxes(long saleId) throws NoContentException, BadRequestException {
        // Deletes all the SaleProducts of a Sale given its id

        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + saleId + " does not exists",
                    42); // Lista en docs
        }

        // Check if the sale is closed
        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + saleId + " is closed", 43); //
        }

        // Check if the sale is canceled
        if (sale.get().getStatus().equals(SaleStatics.Status.cancelled)) {
            throw new BadRequestException(
                    "Sale with id " + saleId + " is canceled", 48);
        }

        // Delete all the SaleProducts of the Sale
        taxRepository.deleteAllBySaleId(saleId);
    }

    public BranchSalesInfoDTO getBranchSales(
            int page,
            int size,
            Long branch_id) throws UnprocessableException, NoContentException {

        Optional<Branch> branch = branchRepository.findById(branch_id);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branch_id + " does not exists",
                    21);
        }
        if (page < 0) {
            throw new UnprocessableException(
                    "Page number cannot be less than zero",
                    44);
        }
        if (size < 1) {
            throw new UnprocessableException(
                    "Page size cannot be less than one",
                    45);
        }

        Date start_time = new Date(0);

        Pageable not_ongoing_sales_paging;

        not_ongoing_sales_paging = PageRequest.of(
                page,
                size,
                Sort.by("startTime").descending());

        Page<Sale> historicSales = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                branch_id,
                List.of(SaleStatics.Status.cancelled, SaleStatics.Status.closed),
                start_time,
                not_ongoing_sales_paging);

        List<Sale> ongoingSales = saleRepository.findAllByTableBranchIdAndStatusOrderByStartTimeDesc(
                branch_id,
                SaleStatics.Status.ongoing);

        List<SaleInfoDTO> notOngoingSalesInfoDTO = new ArrayList<>();
        List<SaleInfoDTO> ongoingSalesInfoDTO = new ArrayList<>();

        historicSales.forEach(sale -> {
            SaleDTO saleDTO = saleMapper.toDTO(sale);
            List<TaxDTO> taxListDTO = getTaxesBySaleId(sale.getId());
            List<SaleProductDTO> saleProductListDTO = getSaleProductsbySaleId(sale.getId());

            // Create a SaleInfo DTO
            SaleInfoDTO saleInfoDTO = SaleInfoDTO.builder()
                    .sale(saleDTO)
                    .taxes(taxListDTO)
                    .products(saleProductListDTO)
                    .build();

            notOngoingSalesInfoDTO.add(saleInfoDTO);
        });

        ongoingSales.forEach(sale -> {
            SaleDTO saleDTO = saleMapper.toDTO(sale);
            List<TaxDTO> taxListDTO = getTaxesBySaleId(sale.getId());
            List<SaleProductDTO> saleProductListDTO = getSaleProductsbySaleId(sale.getId());

            // Create a SaleInfo DTO
            SaleInfoDTO saleInfoDTO = SaleInfoDTO.builder()
                    .sale(saleDTO)
                    .taxes(taxListDTO)
                    .products(saleProductListDTO)
                    .build();
            ongoingSalesInfoDTO.add(saleInfoDTO);
        });

        BranchSalesInfoDTO response = BranchSalesInfoDTO.builder()
                .ongoingSalesInfo(ongoingSalesInfoDTO)
                .historicSalesInfo(notOngoingSalesInfoDTO)
                .currentHistoricPage(page)
                .totalHistoricPages(historicSales.getTotalPages())
                .build();

        return response;
    }
}

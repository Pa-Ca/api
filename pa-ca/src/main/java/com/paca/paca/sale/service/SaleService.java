package com.paca.paca.sale.service;

import java.util.Set;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageImpl;
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
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.sale.dto.BranchSalesInfoDTO;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.reservation.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    private final SaleMapper saleMapper;

    private final TaxMapper taxMapper;

    private final BranchRepository branchRepository;

    private final TableRepository tableRepository;

    private final PaymentOptionRepository paymentOptionRepository;

    private final ReservationRepository reservationRepository;

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
                    42);
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

        // Get the payment option from the paymento option id in the dto
        PaymentOption paymentOption;
        if (dto.getPaymentOptionId() == null) {
            paymentOption = null;
        } else {
            paymentOption = paymentOptionRepository.findById(dto.getPaymentOptionId()).orElseThrow(
                    () -> new NoContentException(
                            "Payment option with id " + dto.getPaymentOptionId() + " does not exists", 58));
        }

        // Get the reservation from the reservation id in the dto
        Reservation reservation;
        if (dto.getReservationId() == null) {
            reservation = null;
        } else {
            reservation = reservationRepository.findById(dto.getReservationId()).orElseThrow(
                    () -> new NoContentException(
                            "Reservation with id " + dto.getReservationId() + " does not exists", 27));
        }

        Sale newSale_ = saleMapper.toEntity(dto, table.get(), reservation, paymentOption);

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
                    42);
        }

        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " can not be canceled because it is already closed", 43);
        }

        Long tableId = sale.get().getTable().getId();
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + tableId + " does not exists",
                    49);
        }

        SaleDTO dto = SaleDTO.builder().status(SaleStatics.Status.cancelled).build();
        Sale updatedSale = saleMapper.updateModel(dto, sale.get(), null);
        saleRepository.save(updatedSale);
    }

    // Do not use this method (not tested)
    public void close(Long id) throws NoContentException, BadRequestException {
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + id + " does not exists",
                    42);
        }

        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " can not be closed because it is already closed", 43);
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
                    "Table with id " + tableId + " does not exists", 49);
        }

        SaleDTO dto = SaleDTO.builder().status(SaleStatics.Status.closed).build();
        Sale updatedSale = saleMapper.updateModel(dto, sale.get(), null);
        saleRepository.save(updatedSale);
    }

    public SaleInfoDTO update(long id, SaleDTO dto) throws NoContentException, BadRequestException {

        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + id + " does not exists",
                    42);
        }

        // Check if the sale is closed
        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is closed", 43);
        }

        // Check if the sale is canceled
        if (sale.get().getStatus().equals(SaleStatics.Status.cancelled)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is canceled", 48);
        }

        // Get the payment option from the paymento option id in the dto
        PaymentOption paymentOption;
        if (dto.getPaymentOptionId() == null) {
            paymentOption = null;
        } else {
            paymentOption = paymentOptionRepository.findById(dto.getPaymentOptionId()).orElseThrow(
                    () -> new NoContentException(
                            "Payment option with id " + dto.getPaymentOptionId() + " does not exists", 58));
        }

        // Update the sale
        Sale updatedSale = saleMapper.updateModel(dto, sale.get(), paymentOption);
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
                    42);
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
                    42);
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
            Long branchId,
            Date startTime,
            Date endTime,
            String fullname,
            String identityDocument) throws UnprocessableException, NoContentException {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
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

        // Apply filters to the historic sales
        Set<Sale> historicSalesConcat = new HashSet<>();
        List<Sale> historicSalesByWord = saleRepository.findAllByTableBranchIdAndFilters(
                branchId,
                List.of(SaleStatics.Status.cancelled, SaleStatics.Status.closed),
                startTime,
                endTime,
                null,
                null,
                identityDocument);
        // Add the sales to the set
        historicSalesConcat.addAll(historicSalesByWord);
        if (fullname != null) {
            // Separate the fullname in tokens to apply the filters dynamically
            for (String word : fullname.split(" ")) {
                historicSalesByWord = saleRepository.findAllByTableBranchIdAndFilters(
                        branchId,
                        List.of(SaleStatics.Status.cancelled, SaleStatics.Status.closed),
                        startTime,
                        endTime,
                        word,
                        null,
                        identityDocument);
                historicSalesByWord.addAll(saleRepository.findAllByTableBranchIdAndFilters(
                        branchId,
                        List.of(SaleStatics.Status.cancelled, SaleStatics.Status.closed),
                        startTime,
                        endTime,
                        word,
                        null,
                        identityDocument));
                historicSalesConcat.retainAll(historicSalesByWord);
            }
        }
        // Create list from set
        List<Sale> historicSales = new ArrayList<>(historicSalesConcat);
        // Sort the list
        historicSales.sort((s1, s2) -> s2.getStartTime().compareTo(s1.getStartTime()));

        // Create a Pageable object for the historic sales
        Pageable paging = PageRequest.of(page, size);
        Page<Sale> historicSalesPage = new PageImpl<>(
                historicSales,
                paging,
                size);

        List<Sale> ongoingSales = saleRepository.findAllByTableBranchIdAndStatusOrderByStartTimeDesc(
                branchId,
                SaleStatics.Status.ongoing);

        List<SaleInfoDTO> notOngoingSalesInfoDTO = new ArrayList<>();
        List<SaleInfoDTO> ongoingSalesInfoDTO = new ArrayList<>();

        historicSalesPage.forEach(sale -> {
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
                .totalHistoricPages(historicSalesPage.getTotalPages())
                .build();

        return response;
    }
}

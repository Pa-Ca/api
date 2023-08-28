package com.paca.paca.sale.service;

import java.util.Set;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.beans.support.PagedListHolder;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.model.SaleTax;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.sale.model.OnlineSale;
import com.paca.paca.sale.model.InsiteSale;
import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.branch.utils.TableMapper;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.reservation.dto.GuestDTO;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.sale.model.InsiteSaleTable;
import com.paca.paca.sale.dto.BranchSalesInfoDTO;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.SaleTaxRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.sale.repository.InsiteSaleRepository;
import com.paca.paca.sale.repository.OnlineSaleRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.exception.exceptions.NotFoundException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.sale.repository.InsiteSaleTableRepository;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.reservation.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final TaxMapper taxMapper;

    private final SaleMapper saleMapper;

    private final TableMapper tableMapper;

    private final GuestMapper guestMapper;

    private final ClientMapper clientMapper;

    private final SaleProductMapper saleProductMapper;

    private final TaxRepository taxRepository;

    private final SaleRepository saleRepository;

    private final TableRepository tableRepository;

    private final BranchRepository branchRepository;

    private final SaleTaxRepository saleTaxRepository;

    private final InsiteSaleRepository insiteSaleRepository;

    private final OnlineSaleRepository onlineSaleRepository;

    private final ReservationRepository reservationRepository;

    private final SaleProductRepository saleProductRepository;

    private final ClientGuestRepository clientGuestRepository;

    private final InsiteSaleTableRepository insiteSaleTableRepository;

    private Boolean isInsite(Long id) throws NotFoundException {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NotFoundException(
                    "Sale with id " + id + " does not exists",
                    42);
        }

        return insiteSaleRepository.findBySaleId(id).isPresent();
    }

    private Reservation getReservationBySaleId(Long id) {
        Optional<InsiteSale> sale = insiteSaleRepository.findBySaleId(id);
        if (sale.isEmpty()) {
            return null;
        }

        return sale.get().getReservation();
    }

    private List<TaxDTO> getTaxesBySaleId(Long id) throws NotFoundException {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NotFoundException(
                    "Sale with id " + id + " does not exists",
                    42);
        }

        List<SaleTax> taxes = saleTaxRepository.findAllBySaleId(id);

        // Map the taxes to DTOs
        List<TaxDTO> response = new ArrayList<>();
        for (SaleTax tax : taxes) {
            TaxDTO dto = taxMapper.toDTO(tax.getTax());
            response.add(dto);
        }

        return response;
    }

    private List<TableDTO> getTablesBySaleId(Long id) {
        Optional<InsiteSale> sale = insiteSaleRepository.findBySaleId(id);
        if (sale.isEmpty()) {
            return new ArrayList<>();
        }

        List<InsiteSaleTable> tables = insiteSaleTableRepository.findAllByInsiteSaleId(sale.get().getId());

        // Map the taxes to DTOs
        List<TableDTO> response = new ArrayList<>();
        for (InsiteSaleTable table : tables) {
            TableDTO dto = tableMapper.toDTO(table.getTable());
            response.add(dto);
        }

        return response;
    }

    private List<SaleProductDTO> getSaleProductsbySaleId(long id)
            throws NotFoundException {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NotFoundException("Sale with id " + id + " does not exists", 42);
        }

        List<SaleProduct> saleProducts = saleProductRepository.findAllBySaleId(id);

        // Map the products to DTOs
        List<SaleProductDTO> response = new ArrayList<>();
        for (SaleProduct saleProduct : saleProducts) {
            SaleProductDTO dto = saleProductMapper.toDTO(saleProduct);
            response.add(dto);
        }

        // Return the DTOs
        return response;
    }

    private SaleInfoDTO completeData(long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with id " + id + " does not exists", 42));

        SaleDTO dto = saleMapper.toDTO(sale);
        Boolean insite = isInsite(sale.getId());
        Reservation reservation = getReservationBySaleId(sale.getId());
        List<TaxDTO> taxListDTO = getTaxesBySaleId(sale.getId());
        List<TableDTO> tableListDTO = getTablesBySaleId(sale.getId());
        List<SaleProductDTO> saleProductListDTO = getSaleProductsbySaleId(sale.getId());
        GuestDTO guestDTO = sale.getClientGuest().getHaveGuest() ? guestMapper.toDTO(sale.getClientGuest().getGuest())
                : null;
        ClientDTO clientDTO = !sale.getClientGuest().getHaveGuest()
                ? clientMapper.toDTO(sale.getClientGuest().getClient())
                : null;

        // Create a SaleInfo DTO
        return new SaleInfoDTO(
                dto,
                insite,
                guestDTO,
                clientDTO,
                reservation == null ? null : reservation.getId(),
                taxListDTO,
                tableListDTO,
                saleProductListDTO);
    }

    public SaleInfoDTO save(SaleInfoDTO dto) throws NotFoundException {
        Optional<Branch> branch = branchRepository.findById(dto.getSale().getBranchId());
        if (branch.isEmpty()) {
            throw new NotFoundException(
                    "Branch with id " + dto.getSale().getBranchId() + " does not exists", 20);
        }

        Optional<ClientGuest> clientGuest = clientGuestRepository.findById(dto.getSale().getClientGuestId());
        if (clientGuest.isEmpty()) {
            throw new NotFoundException(
                    "Client guest with id " + dto.getSale().getClientGuestId() + " does not exists", 59);
        }

        dto.getSale().setStartTime(new Date());
        Sale sale = saleMapper.toEntity(dto.getSale(), branch.get(), null, clientGuest.get());
        Sale newSale = saleRepository.save(sale);

        // Create taxes
        taxRepository.findAllByBranchId(newSale.getBranch().getId()).forEach(tax -> {
            Tax newTax = Tax.builder()
                    .name(tax.getName())
                    .type(tax.getType())
                    .value(tax.getValue())
                    .build();
            taxRepository.save(newTax);

            SaleTax saleTax = SaleTax.builder()
                    .sale(newSale)
                    .tax(newTax)
                    .build();
            saleTaxRepository.save(saleTax);
        });

        if (dto.getInsite()) {
            Reservation reservation;
            if (dto.getReservationId() == null) {
                reservation = null;
            } else {
                reservation = reservationRepository.findById(dto.getReservationId()).orElseThrow(
                        () -> new NotFoundException(
                                "Reservation with id " + dto.getReservationId() + " does not exists", 27));
            }

            InsiteSale insiteSale = InsiteSale.builder()
                    .sale(newSale)
                    .reservation(reservation)
                    .build();
            InsiteSale newInsiteSale = insiteSaleRepository.save(insiteSale);

            // Create tables
            dto.getTables().forEach(tableDTO -> {
                Optional<Table> table = tableRepository.findById(tableDTO.getId());
                if (table.isEmpty()) {
                    throw new NotFoundException(
                            "Table with id " + tableDTO.getId() + " does not exists", 49);
                }

                InsiteSaleTable insiteSaleTable = InsiteSaleTable.builder()
                        .insiteSale(newInsiteSale)
                        .table(table.get())
                        .build();
                insiteSaleTableRepository.save(insiteSaleTable);
            });
        } else {
            OnlineSale onlineSale = OnlineSale.builder()
                    .sale(sale)
                    .build();
            onlineSale = onlineSaleRepository.save(onlineSale);
        }

        return completeData(newSale.getId());
    }

    public SaleInfoDTO update(long id, SaleDTO dto) throws NotFoundException, BadRequestException {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NotFoundException(
                    "Sale with id " + id + " does not exists",
                    42);
        }
        if (sale.get().getStatus().equals(SaleStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is closed", 43);
        }
        if (sale.get().getStatus().equals(SaleStatics.Status.CANCELLED)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is cancelled", 48);
        }

        // Update the sale
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        updatedSale = saleRepository.save(updatedSale);

        return completeData(updatedSale.getId());
    }

    public void delete(Long id) throws NotFoundException {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NotFoundException(
                    "Sale with id " + id + " does not exists",
                    42);
        }
        if (sale.get().getStatus().equals(SaleStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is closed", 43);
        }
        if (sale.get().getStatus().equals(SaleStatics.Status.CANCELLED)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is cancelled", 48);
        }

        saleRepository.deleteById(id);
    }

    public void clearSaleProducts(long id) throws NotFoundException, BadRequestException {
        Optional<Sale> sale = saleRepository.findById(id);
        if (sale.isEmpty()) {
            throw new NotFoundException(
                    "Sale with id " + id + " does not exists",
                    42);
        }
        if (sale.get().getStatus().equals(SaleStatics.Status.CLOSED)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is closed",
                    43);
        }
        if (sale.get().getStatus().equals(SaleStatics.Status.CANCELLED)) {
            throw new BadRequestException(
                    "Sale with id " + id + " is cancelled",
                    48);
        }
        saleProductRepository.deleteAllBySaleId(id);
    }

    public BranchSalesInfoDTO getBranchSales(
            int page,
            int size,
            Long branchId,
            Date startTime,
            Date endTime,
            String fullname,
            String identityDocument) throws UnprocessableException, NotFoundException {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NotFoundException(
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
        List<Sale> historicSalesByWord = saleRepository.findAllByBranchIdAndFilters(
                branchId,
                List.of(SaleStatics.Status.CANCELLED, SaleStatics.Status.CLOSED),
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
                historicSalesByWord = saleRepository.findAllByBranchIdAndFilters(
                        branchId,
                        List.of(SaleStatics.Status.CANCELLED, SaleStatics.Status.CLOSED),
                        startTime,
                        endTime,
                        word,
                        null,
                        identityDocument);
                historicSalesByWord.addAll(saleRepository.findAllByBranchIdAndFilters(
                        branchId,
                        List.of(SaleStatics.Status.CANCELLED, SaleStatics.Status.CLOSED),
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

        // Create a Pageable object for the historic sales=
        PagedListHolder<Sale> historicSalesPage = new PagedListHolder<Sale>(historicSales);
        historicSalesPage.setPageSize(size);
        historicSalesPage.setPage(page);

        List<Sale> ongoingSales = saleRepository.findAllByBranchIdAndStatusOrderByStartTimeDesc(
                branchId,
                SaleStatics.Status.ONGOING);

        List<SaleInfoDTO> historicSalesInfoDTO = new ArrayList<>();
        List<SaleInfoDTO> ongoingSalesInfoDTO = new ArrayList<>();

        historicSalesPage.getPageList().forEach(sale -> {
            historicSalesInfoDTO.add(completeData(sale.getId()));
        });

        ongoingSales.forEach(sale -> {
            ongoingSalesInfoDTO.add(completeData(sale.getId()));
        });

        BranchSalesInfoDTO response = BranchSalesInfoDTO.builder()
                .ongoingSalesInfo(ongoingSalesInfoDTO)
                .historicSalesInfo(historicSalesInfoDTO)
                .currentHistoricPage(page)
                .totalHistoricPages(historicSalesPage.getPageCount())
                .totalHistoricElements(historicSales.size())
                .build();

        return response;
    }
}

package com.paca.paca.sale.service;


import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;


import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.dto.BranchSalesDTO;
import com.paca.paca.sale.dto.SaleDTO;

import com.paca.paca.sale.dto.SaleListDTO;

import com.paca.paca.sale.statics.SaleStatics;

import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.exception.exceptions.BadRequestException;
// Import exceptions
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;



@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    private final BranchRepository branchRepository;

    private final TableRepository tableRepository;

    public SaleListDTO getSalesPage(int page,
            int size,
            Long branch_id,
            String sorting_by,
            boolean ascending,
            Date start_time,
            Date end_time,
            Integer status
            ) throws UnprocessableException, NoContentException {

        // Now lets add the exeption handling
        if (page < 0) {
            throw new UnprocessableException(
                    "Page number cannot be less than zero",
                    44);
        }
        if (size < 1) {
            throw new UnprocessableException(
                    "Page Page size cannot be less than one",
                    45);
        }

        // Check if sorting_by is in BranchStatics.BranchSortingKeys
        if (!SaleStatics.SaleSortingKeys.contains(sorting_by)) {
            throw new UnprocessableException(
                    "Sorting key is not valid",
                    46);
        }

        // Create a Pageable object that specifies the page and size parameters as well
        // as a sort
        // order for the results
        Pageable paging;
        if (ascending) {
            paging = PageRequest.of(
                    page,
                    size,
                    Sort.by(sorting_by).ascending());
        } else {
            paging = PageRequest.of(
                    page,
                    size,
                    Sort.by(sorting_by).descending());
        }

        if (start_time == null) {
            start_time = new Date(0);
        }
        if (end_time == null) {
            end_time = new Date(Long.MAX_VALUE);
        }  

        // Lets apply the filters
        Page<Sale> pagedResult = saleRepository
                .findAllByTableBranchIdAndStatusAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                        branch_id,
                        status,
                        start_time,
                        end_time,
                        paging);

        List<SaleDTO> response = new ArrayList<>();

        pagedResult.forEach(sale -> {
            SaleDTO dto = saleMapper.toDTO(sale);
            response.add(dto);
        });

        return SaleListDTO.builder().sales(response).build();
    }

    public SaleDTO save(SaleDTO dto) throws NoContentException, BadRequestException {
        
        Optional<Table> table = tableRepository.findById(dto.getTableId());
        
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + dto.getTableId() + " does not exists",49
                    ); // !
        }

        Sale newSale;
        
        newSale = saleMapper.toEntity(dto, table.get(), null);

        newSale = saleRepository.save(newSale);

        SaleDTO dtoResponse = saleMapper.toDTO(newSale);

        return dtoResponse;
    }
    
    public void cancel(Long id) throws NoContentException, BadRequestException {
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + id + " does not exists",
                    42); // Lista en docs
        }

        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " can not be canceled because it is already closed", 43
                    ); // Lista en docs
        }

        Long tableId = sale.get().getTable().getId();
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + tableId + " does not exists",
                    49); // Lista en docs!
        }

        SaleDTO dto = SaleDTO.builder().status(SaleStatics.Status.canceled).build();
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        saleRepository.save(updatedSale);
    }

    public void close(Long id) throws NoContentException, BadRequestException {
        Optional<Sale> sale = saleRepository.findById(id);

        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + id + " does not exists",
                    42); // Lista en docs
        }

        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + id + " can not be closed because it is already closed", 43
                    ); // Lista en docs
        }

        // add exception (user cannot close a canceled sale)

        if (sale.get().getStatus().equals(SaleStatics.Status.canceled)){
            throw new BadRequestException(
                "Sale with id " + id + " can not be closed because it was canceled", 48
            );
        }

        Long tableId = sale.get().getTable().getId();
        Optional<Table> table = tableRepository.findById(tableId);
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + tableId + " does not exists",49
                    ); // Lista en docs!
        }

        SaleDTO dto = SaleDTO.builder().status(SaleStatics.Status.closed).build();
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        saleRepository.save(updatedSale);
    }

    public SaleDTO update(SaleDTO dto) throws NoContentException, BadRequestException {
        
        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(dto.getId());
        if (sale.isEmpty()) {
            throw new NoContentException(
                    "Sale with id " + dto.getId() + " does not exists",
                    42); // Lista en docs
        }

        // Check if the table exists
        Optional<Table> table = tableRepository.findById(dto.getTableId());
        if (table.isEmpty()) {
            throw new NoContentException(
                    "Table with id " + dto.getTableId() + " does not exists",
                    49); // Lista en docs!
        }

        // Check if the sale is closed
        if (sale.get().getStatus().equals(SaleStatics.Status.closed)) {
            throw new BadRequestException(
                    "Sale with id " + dto.getId() + " can not be updated because it is already closed", 43
                    ); // Lista en docs
        }

        // Check if the sale is canceled
        if (sale.get().getStatus().equals(SaleStatics.Status.canceled)) {
            throw new BadRequestException(
                    "Sale with id " + dto.getId() + " can not be updated because it was canceled", 48
                    ); // Lista en docs
        }

        // Update the sale
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        updatedSale = saleRepository.save(updatedSale);
        SaleDTO dtoResponse = saleMapper.toDTO(updatedSale);


        return dtoResponse;
    }

    public BranchSalesDTO getBranchSales(
            int page,
            int size,
            Long branch_id
            ) throws UnprocessableException, NoContentException {

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
                            "Page Page size cannot be less than one",
                            45);
                }
                
                Date start_time = new Date(0);
                Date end_time = new Date(Long.MAX_VALUE);
                

                Pageable not_ongoing_sales_paging;
                
                not_ongoing_sales_paging = PageRequest.of(
                        page,
                        size,
                        Sort.by("start_time").descending());
                

                Page<Sale> historicSales = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branch_id,
                List.of(SaleStatics.Status.canceled, SaleStatics.Status.closed),
                start_time,
                end_time,
                not_ongoing_sales_paging);

                List<Sale> ongoingSales = saleRepository.findAllByTableBranchIdAndStatusOrderByStartTimeDesc(
                    branch_id,
                    SaleStatics.Status.ongoing
                );
                
                List<SaleDTO> notOngoingSalesDTO = new ArrayList<>();
                List<SaleDTO> ongoingSalesDTO = new ArrayList<>();

                historicSales.forEach(sale -> {
                    SaleDTO dto = saleMapper.toDTO(sale);
                    notOngoingSalesDTO.add(dto);
                });

                ongoingSales.forEach(sale -> {
                    SaleDTO dto = saleMapper.toDTO(sale);
                    ongoingSalesDTO.add(dto);
                });

                BranchSalesDTO response = BranchSalesDTO.builder()
                .ongoingSales(ongoingSalesDTO)
                .historicSales(notOngoingSalesDTO)
                .build();

                return response;
            }
}

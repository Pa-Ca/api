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

import com.paca.paca.sale.dto.SaleDTO;

import com.paca.paca.sale.dto.SaleListDTO;

import com.paca.paca.sale.statics.SaleStatics;

import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.repository.BranchRepository;
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
                .findAllByBranchIdAndStatusAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
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
        
        Optional<Branch> branch = branchRepository.findById(dto.getBranchId());
        
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + dto.getBranchId() + " does not exists",
                    21); // Lista en docs!
        }

        Sale newSale;
        
        newSale = saleMapper.toEntity(dto, branch.get());

        newSale = saleRepository.save(newSale);

        SaleDTO dtoResponse = saleMapper.toDTO(newSale);
        // dtoResponse.completeData(guestRepository, clientGroupRepository, clientRepository);

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

        Long branchId = sale.get().getBranch().getId();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isEmpty()) {
            throw new NoContentException(
                    "Branch with id " + branchId + " does not exists",
                    21); // Lista en docs!
        }

        SaleDTO dto = SaleDTO.builder().status(SaleStatics.Status.canceled).build();
        Sale updatedSale = saleMapper.updateModel(dto, sale.get());
        saleRepository.save(updatedSale);
    }

}

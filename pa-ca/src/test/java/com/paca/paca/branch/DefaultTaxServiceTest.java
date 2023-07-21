package com.paca.paca.branch;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.branch.service.DefaultTaxService;
import com.paca.paca.branch.utils.DefaultTaxMapper;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class DefaultTaxServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private DefaultTaxRepository defaultTaxRepository;

    @Mock
    private DefaultTaxMapper defaultTaxMapper;


    @InjectMocks
    private DefaultTaxService defaultTaxService;
    

    private TestUtils utils = TestUtils.builder().build();


    @Test
    void shouldSave() {
        DefaultTaxDTO  defaultTaxDTO = utils.createDefaultTaxDTO(null);
        DefaultTax defaultTax = utils.createDefaultTax(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(defaultTaxRepository.save(any())).thenReturn(defaultTax);
        when(defaultTaxMapper.toDTO(any())).thenReturn(defaultTaxDTO);
        
        DefaultTaxDTO  response = defaultTaxService.save(defaultTaxDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToBranchNotExistingInSave(){
        DefaultTaxDTO  defaultTaxDTO = utils.createDefaultTaxDTO(null);   
        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());
        try {
            defaultTaxService.save(defaultTaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Branch with id " + defaultTaxDTO.getBranchId() + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }

    }

    @Test 
    void shouldGetBadRequestExceptionDueToInvalidTaxType() {
        DefaultTaxDTO defaultTaxDTO = utils.createDefaultTaxDTO(null);
        int invalidTaxType = 9856897;
        defaultTaxDTO.setType(invalidTaxType);
        Branch branch = utils.createBranch(null);
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        try{
            defaultTaxService.save(defaultTaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Invalid default tax type:" + invalidTaxType, e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 51);
        }
    }

    @Test
    void shouldUpdate(){
        DefaultTaxDTO  defaultTaxDTO = utils.createDefaultTaxDTO(null);
        DefaultTax defaultTax = utils.createDefaultTax(null);

        when(defaultTaxRepository.findById(anyLong())).thenReturn(Optional.of(defaultTax));
        when(defaultTaxRepository.save(any())).thenReturn(defaultTax);
        when(defaultTaxMapper.toDTO(any())).thenReturn(defaultTaxDTO);
        when(defaultTaxMapper.updateModel(any(), any())).thenReturn(defaultTax);
        
        DefaultTaxDTO  response = defaultTaxService.update(1L ,defaultTaxDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldDelete(){
        DefaultTax defaultTax = utils.createDefaultTax(null);

        when(defaultTaxRepository.findById(1L)).thenReturn(Optional.of(defaultTax));

        defaultTaxService.delete(1L);

        verify(defaultTaxRepository, times(1)).deleteById(1L);
    }

}
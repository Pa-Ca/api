package com.paca.paca.branch;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import com.paca.paca.ServiceTest;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.dto.DefaultTaxDTO;
import com.paca.paca.branch.service.DefaultTaxService;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;

public class DefaultTaxServiceTest extends ServiceTest {

    @InjectMocks
    private DefaultTaxService defaultTaxService;

    @Test
    void shouldSave() {
        Tax tax = utils.createTax();
        Branch branch = utils.createBranch(null);
        DefaultTax defaultTax = utils.createDefaultTax(tax, branch);
        DefaultTaxDTO defaultTaxDTO = utils.createDefaultTaxDTO(defaultTax);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(taxMapper.toEntity(any(TaxDTO.class))).thenReturn(tax);
        when(taxRepository.save(any(Tax.class))).thenReturn(tax);
        when(defaultTaxRepository.save(any(DefaultTax.class))).thenReturn(defaultTax);
        when(taxMapper.toDTO(any(Tax.class))).thenReturn(defaultTaxDTO.getTax());

        TaxDTO response = defaultTaxService.save(defaultTaxDTO);

        assertThat(response).isEqualTo(defaultTaxDTO.getTax());
    }

    @Test
    void shouldGetNoContentExceptionDueToBranchNotExistingInSave() {
        DefaultTaxDTO defaultTaxDTO = utils.createDefaultTaxDTO(null);
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
    void shouldGetBadRequestExceptionDueToInvalidTaxTypeInSave() {
        Branch branch = utils.createBranch(null);
        DefaultTaxDTO defaultTaxDTO = utils.createDefaultTaxDTO(null);
        Short invalidTaxType = 1000;
        defaultTaxDTO.getTax().setType(invalidTaxType);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));

        try {
            defaultTaxService.save(defaultTaxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Invalid tax type: " + invalidTaxType, e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 51);
        }
    }

    @Test
    void shouldUpdate() {
        Tax tax = utils.createTax();
        TaxDTO dto = utils.createTaxDTO(tax);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));
        when(taxMapper.updateModel(any(TaxDTO.class), any(Tax.class))).thenReturn(tax);
        when(taxRepository.save(any(Tax.class))).thenReturn(tax);
        when(taxMapper.toDTO(any(Tax.class))).thenReturn(dto);

        TaxDTO response = defaultTaxService.update(tax.getId(), dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentExceptionDueToDefaultTaxNotExistingInUpdate() {
        TaxDTO dto = utils.createTaxDTO(null);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            defaultTaxService.update(1L, dto);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Tax with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 52);
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToDefaultInvalidTaxTypeInUpdate() {
        Tax tax = utils.createTax();
        TaxDTO taxDTO = utils.createTaxDTO(tax);
        Short invalidTaxType = (short) 696969;
        taxDTO.setType(invalidTaxType);

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));

        try {
            defaultTaxService.update(1L, taxDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Invalid tax type: " + invalidTaxType, e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 51);
        }
    }

    @Test
    void shouldDelete() {
        Tax tax = utils.createTax();

        when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));

        defaultTaxService.delete(tax.getId());

        verify(taxRepository, times(1)).deleteById(tax.getId());
    }

    @Test
    void shouldGetNoContentExcepetionDueToDefaultTaxNotExistingInDelete(){
        when(taxRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            defaultTaxService.delete(1L);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Tax with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 52);
        }
    }

}
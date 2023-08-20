package com.paca.paca.branch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import com.paca.paca.ServiceTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.branch.service.PaymentOptionService;
import com.paca.paca.exception.exceptions.NoContentException;

public class PaymentOptionServiceTest extends ServiceTest {

    @InjectMocks
    private PaymentOptionService paymentOptionService;

    @Test
    void shouldSave() {
        PaymentOptionDTO dto = utils.createPaymentOptionDTO(null);
        PaymentOption paymentOption = utils.createPaymentOption(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(paymentOptionRepository.save(any())).thenReturn(paymentOption);
        when(paymentOptionMapper.toDTO(any())).thenReturn(dto);

        PaymentOptionDTO response = paymentOptionService.save(dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentExceptionDueToBranchNotExistingInSave() {
        PaymentOptionDTO paymentOptionDTO = utils.createPaymentOptionDTO(null);
        Long branchId = paymentOptionDTO.getBranchId();

        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            paymentOptionService.save(paymentOptionDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branchId + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldUpdate() {
        PaymentOptionDTO dto = utils.createPaymentOptionDTO(null);
        PaymentOption paymentOption = utils.createPaymentOption(null);

        when(paymentOptionRepository.findById(anyLong())).thenReturn(Optional.of(paymentOption));
        when(paymentOptionRepository.save(any())).thenReturn(paymentOption);
        when(paymentOptionMapper.toDTO(any())).thenReturn(dto);
        when(paymentOptionMapper.updateModel(any(), any())).thenReturn(paymentOption);

        PaymentOptionDTO response = paymentOptionService.update(1L, dto);

        assertThat(response).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentExceptionDueToBranchNotExistingInUpdate() {
        PaymentOptionDTO paymentOptionDTO = utils.createPaymentOptionDTO(null);
        Long branchId = paymentOptionDTO.getBranchId();

        when(paymentOptionRepository.findById(anyLong())).thenReturn(Optional.of(new PaymentOption()));

        try {
            paymentOptionService.update(1L, paymentOptionDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch with id " + branchId + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetNoContentExceptionDueToPaymentOptionNotExistingInUpdate() {
        PaymentOptionDTO paymentOptionDTO = utils.createPaymentOptionDTO(null);
        long paymentOptionId = paymentOptionDTO.getId();

        when(paymentOptionRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            paymentOptionService.update(paymentOptionId, paymentOptionDTO);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Payment option with id " + paymentOptionId + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 58);
        }
    }

    @Test
    void shouldDelete() {
        PaymentOption paymentOption = utils.createPaymentOption(null);

        when(paymentOptionRepository.findById(1L)).thenReturn(Optional.of(paymentOption));

        paymentOptionService.delete(1L);

        verify(paymentOptionRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldGetNoContentExceptionDueToPaymentOptionNotExistingInDelete() {
        long paymentOptionId = 12L;

        when(paymentOptionRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            paymentOptionService.delete(paymentOptionId);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Payment option with id " + paymentOptionId + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 58);
        }
    }

}

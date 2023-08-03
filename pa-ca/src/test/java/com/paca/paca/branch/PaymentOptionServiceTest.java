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

import com.paca.paca.branch.dto.PaymentOptionDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.branch.service.PaymentOptionService;
import com.paca.paca.branch.utils.PaymentOptionMapper;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class PaymentOptionServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private PaymentOptionRepository paymentOptionRepository;

    @Mock
    private PaymentOptionMapper paymentOptionMapper;


    @InjectMocks
    private PaymentOptionService paymentOptionService;
    

    private TestUtils utils = TestUtils.builder().build();


    @Test
    void shouldSave() {
        PaymentOptionDTO  paymentOptionDTO = utils.createPaymentOptionDTO(null);
        PaymentOption paymentOption = utils.createPaymentOption(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(paymentOptionRepository.save(any())).thenReturn(paymentOption);
        when(paymentOptionMapper.toDTO(any())).thenReturn(paymentOptionDTO);
        
        PaymentOptionDTO  response = paymentOptionService.save(paymentOptionDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToBranchNotExistingInSave(){
        PaymentOptionDTO  paymentOptionDTO = utils.createPaymentOptionDTO(null);
        Long branchId = paymentOptionDTO.getBranchId();
        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());  
        try {
            paymentOptionService.save(paymentOptionDTO);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Branch with id " + branchId + " does not exists");
        }
    }


    @Test
    void shouldUpdate(){
        PaymentOptionDTO  paymentOptionDTO = utils.createPaymentOptionDTO(null);
        PaymentOption paymentOption = utils.createPaymentOption(null);
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(paymentOptionRepository.findById(anyLong())).thenReturn(Optional.of(paymentOption));
        when(paymentOptionRepository.save(any())).thenReturn(paymentOption);
        when(paymentOptionMapper.toDTO(any())).thenReturn(paymentOptionDTO);
        when(paymentOptionMapper.updateModel(any(), any())).thenReturn(paymentOption);
        
        PaymentOptionDTO  response = paymentOptionService.update(1L ,paymentOptionDTO);

        assertThat(response).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToBranchNotExistingInUpdate(){
        PaymentOptionDTO  paymentOptionDTO = utils.createPaymentOptionDTO(null);
        Long branchId = paymentOptionDTO.getBranchId();
        when(paymentOptionRepository.findById(anyLong())).thenReturn(Optional.of(new PaymentOption()));
        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());  
        try {
            paymentOptionService.update(1L ,paymentOptionDTO);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Branch with id " + branchId + " does not exists");
        }
    }

    // @Test
    // void shouldGetNoContentExceptionDueToPaymentOptionNotExistingInUpdate(){
    //     PaymentOptionDTO  paymentOptionDTO = utils.createPaymentOptionDTO(null);
    //     Long paymentOptionId = paymentOptionDTO.getId();
    //     when(branchRepository.findById(anyLong())).thenReturn(Optional.of(new Branch()));  
    //     when(paymentOptionRepository.findById(anyLong())).thenReturn(Optional.empty());  
    //     try {
    //         paymentOptionService.update(1L ,paymentOptionDTO);
    //     } catch (Exception e) {
    //         assertThat(e.getMessage()).isEqualTo("Payment option with id " + paymentOption + " does not exists");
    //     }
    // }

    @Test
    void shouldDelete(){
        PaymentOption paymentOption = utils.createPaymentOption(null);

        when(paymentOptionRepository.findById(1L)).thenReturn(Optional.of(paymentOption));

        paymentOptionService.delete(1L);

        verify(paymentOptionRepository, times(1)).deleteById(1L);
    }

}

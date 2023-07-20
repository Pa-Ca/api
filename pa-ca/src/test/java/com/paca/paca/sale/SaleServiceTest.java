package com.paca.paca.sale;

import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.model.DefaultTax;
import com.paca.paca.branch.model.Table;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.sale.dto.BranchSalesInfoDTO;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.model.Tax;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.repository.ReservationRepository;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleMapper saleMapper;

    @Mock
    private TaxMapper taxMapper;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private TaxRepository taxRepository;

    @Mock
    private SaleProductRepository saleProductRepository;

    @Mock
    private SaleProductMapper saleProductMapper;

    @Mock
    private PaymentOptionRepository paymentOptionRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private DefaultTaxRepository defaultTaxRepository;

    @InjectMocks
    private SaleService saleService;
    

    private TestUtils utils = TestUtils.builder().build();

    @Test
    void shouldGetTaxesBySaleId() {

        List<Tax> taxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));
        Sale sale = utils.createSale(null, null, null);

        when(taxRepository.findAllBySaleId(any())).thenReturn(taxes);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        List<TaxDTO> responseDTO = saleService.getTaxesBySaleId(1L);

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInGetTaxesBySaleId(){
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            saleService.getTaxesBySaleId(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldDeleteSalebyId() {

        Sale sale = utils.createSale(null, null, null);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        saleService.delete(1L);

        verify(saleRepository, times(1)).deleteById(1L);

    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInDelete(){
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            saleService.delete(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldGetSaleProductsbySaleId(){
        Sale sale = utils.createSale(null, null, null);

        List<SaleProduct> saleProducts = TestUtils.castList(SaleProduct.class, Mockito.mock(List.class));

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleProductRepository.findAllBySaleId(1L)).thenReturn(saleProducts);

        List<SaleProductDTO> saleProductList = saleService.getSaleProductsbySaleId(1L);

        assertThat(saleProductList).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInGetSaleProductsbySaleId(){
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            saleService.getSaleProductsbySaleId(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldUpdate() {
        SaleDTO saleDTO = utils.createSaleDTO(null, null);
        Sale sale = utils.createSale(null, null, null);
        List<Tax> taxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));
        SaleProduct saleProduct = utils.createSaleProduct(sale, null);

        List<SaleProduct> saleProducts = List.of(saleProduct);

        when(saleProductRepository.findAllBySaleId(anyLong())).thenReturn(saleProducts);
        when(taxRepository.findAllBySaleId(anyLong())).thenReturn(taxes);
        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));
        when(saleRepository.save(any())).thenReturn(sale);
        when(saleMapper.toDTO(any())).thenReturn(saleDTO);
        when(saleMapper.updateModel(any(), any(), any())).thenReturn(sale);

        when(paymentOptionRepository.findById(any())).thenReturn(Optional.of(new PaymentOption()));


        SaleInfoDTO saleProductDTO = saleService.update(1L, saleDTO);

        assertThat(saleProductDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExistingInUpdate() {
        SaleDTO saleDTO = utils.createSaleDTO(null, null);

        when(saleRepository.findById(any())).thenReturn(Optional.empty());

        try {
            saleService.update(1L, saleDTO);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleBeingClosed() {
        SaleDTO saleDTO = utils.createSaleDTO(null, null);
        // Create a closed sale
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.closed);
        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));

        try {
            saleService.update(1L, saleDTO);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is closed", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 43);
        }
    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleBeingCanceled() {
        SaleDTO saleDTO = utils.createSaleDTO(null, null);
        // Create a closed sale
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.canceled);
        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));

        try {
            saleService.update(1L, saleDTO);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is canceled", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 48);
        }
    }

    @Test
    void shouldSave() {
        Table table = utils.createTable(null);
        SaleDTO saleDTO = utils.createSaleDTO(null, null);
        Sale sale = utils.createSale(null, null, null);
        List<Tax> taxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));
        List<DefaultTax> defaultTaxes = TestUtils.castList(DefaultTax.class, Mockito.mock(List.class));
        SaleProduct saleProduct = utils.createSaleProduct(sale, null);
        List<SaleProduct> saleProducts = List.of(saleProduct);

        when(defaultTaxRepository.findAllByBranchId(anyLong())).thenReturn(defaultTaxes);
        when(saleProductRepository.findAllBySaleId(anyLong())).thenReturn(saleProducts);
        when(tableRepository.findById(anyLong())).thenReturn(Optional.of(table));
        when(taxRepository.findAllBySaleId(anyLong())).thenReturn(taxes);
        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));
        when(reservationRepository.findById(any())).thenReturn(Optional.of(new Reservation()));
        when(paymentOptionRepository.findById(any())).thenReturn(Optional.of(new PaymentOption()));
        when(saleRepository.save(any())).thenReturn(sale);
        when(saleMapper.toDTO(any())).thenReturn(saleDTO);

        SaleInfoDTO saleDto = saleService.save(saleDTO);

        assertThat(saleDto).isNotNull();
    }

    @Test
    void shouldGetNoContentExceptionDueToTableNotExistingInSave() {
        SaleDTO saleDTO = utils.createSaleDTO(null, null);

        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleService.save(saleDTO);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Table with id " + saleDTO.getTableId() + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 49);
        }
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExisitingInClearSaleProducts(){
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleService.clearSaleProducts(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }

    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleBeingClosedInClearSaleProducts(){
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.closed);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.clearSaleProducts(1L);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is closed", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 43);
        }

    }

    @Test 
    void shouldGetBadRequestExceptionDueToSaleBeingCanceledInClearSaleProducts(){
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.canceled);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.clearSaleProducts(1L);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is canceled", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 48);
        }

    }

    @Test
    void shouldClearSaleProducts(){
        Sale sale = utils.createSale(null, null, null);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        saleService.clearSaleProducts(sale.getId());

        verify(saleProductRepository, times(1)).deleteAllBySaleId(sale.getId());
    }

    @Test
    void shouldGetNoContentExceptionDueToSaleNotExisitingInClearSaleTaxes(){
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleService.clearSaleTaxes(1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }

    }

    @Test
    void shouldGetBadRequestExceptionDueToSaleBeingClosedInClearSaleTaxes(){
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.closed);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.clearSaleTaxes(1L);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is closed", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 43);
        }

    }

    @Test 
    void shouldGetBadRequestExceptionDueToSaleBeingCanceledInClearSaleTaxes(){
        Sale sale = utils.createSale(null, null, null);
        sale.setStatus(SaleStatics.Status.canceled);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.clearSaleTaxes(1L);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is canceled", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 48);
        }

    }

    @Test
    void shouldClearSaleTaxes(){
        Sale sale = utils.createSale(null, null, null);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        saleService.clearSaleTaxes(sale.getId());

        verify(taxRepository, times(1)).deleteAllBySaleId(sale.getId());
    }

    @Test
    void shouldGetNoContentExceptionDueToBranchNotExistingInGetBranchSales(){
        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleService.getBranchSales(1, 10, 1L);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Branch with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 21);
        }
    }

    @Test
    void shouldGetUnprocessableExceptionDueToPageNumberBeingLessThanZeroInGetBranchSales(){
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(new Branch()));

        try {
            saleService.getBranchSales(-1, 10, 1L);
        } catch (UnprocessableException e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals("Page number cannot be less than zero", e.getMessage());
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 44);
        }
    }

    @Test
    void shouldGetUnprocessableExceptionDueToSizeBeingLessThanOneInGetBranchSales(){
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(new Branch()));

        try {
            saleService.getBranchSales(1, 0, 1L);
        } catch (UnprocessableException e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals("Page size cannot be less than one", e.getMessage());
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 45);
        }
    }

    @Test
    void shouldGetBranchSales() {

        Branch branch = utils.createBranch(null);
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));

        Page<Sale> salePage = new PageImpl<>(new ArrayList<>());
        List<Sale> currentSales = new ArrayList<>();

        when(saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                anyLong(), anyList(), any(), any())).thenReturn(salePage);

        when(saleRepository.findAllByTableBranchIdAndStatusOrderByStartTimeDesc(
                anyLong(), any())).thenReturn(currentSales);

        BranchSalesInfoDTO branchSales = saleService.getBranchSales(1, 10, branch.getId());

        // Check that the branchSales are not null
        Assert.assertNotNull(branchSales);

    }
}

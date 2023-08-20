package com.paca.paca.sale;

import com.paca.paca.sale.model.Tax;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.sale.dto.TaxDTO;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.dto.SaleDTO;
import com.paca.paca.sale.model.SaleTax;
import com.paca.paca.branch.dto.TableDTO;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.sale.dto.SaleInfoDTO;
import com.paca.paca.sale.utils.TaxMapper;
import com.paca.paca.sale.utils.SaleMapper;
import com.paca.paca.sale.model.InsiteSale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.sale.service.SaleService;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.client.model.ClientGuest;
import com.paca.paca.sale.model.InsiteSaleTable;
import com.paca.paca.sale.dto.BranchSalesInfoDTO;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.sale.repository.TaxRepository;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.sale.repository.SaleTaxRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.sale.repository.InsiteSaleRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.branch.repository.DefaultTaxRepository;
import com.paca.paca.client.repository.ClientGuestRepository;
import com.paca.paca.exception.exceptions.NoContentException;
import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.sale.repository.InsiteSaleTableRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.exception.exceptions.UnprocessableException;
import com.paca.paca.reservation.repository.ReservationRepository;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
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
    private ClientGuestRepository clientGuestRepository;

    @Mock
    private InsiteSaleRepository insiteSaleRepository;

    @Mock
    private PaymentOptionRepository paymentOptionRepository;

    @Mock
    private InsiteSaleTableRepository insiteSaleTableRepository;

    @Mock
    private SaleTaxRepository saleTaxRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private DefaultTaxRepository defaultTaxRepository;

    @InjectMocks
    private SaleService saleService;

    private TestUtils utils = TestUtils.builder().build();

    private SaleInfoDTO completeData(Sale sale, Boolean insite) {
        SaleDTO saleDTO = utils.createSaleDTO(sale);
        Optional<InsiteSale> insiteSale = insite ? Optional.of(utils.createInsiteSale(sale, null)) : Optional.empty();
        List<SaleTax> taxes = TestUtils.castList(
                SaleTax.class,
                Mockito.mock(List.class));
        List<SaleProduct> products = TestUtils.castList(
                SaleProduct.class,
                Mockito.mock(List.class));

        when(saleRepository.findById(any())).thenReturn(Optional.of(sale));
        when(saleMapper.toDTO(any())).thenReturn(saleDTO);
        when(insiteSaleRepository.findBySaleId(anyLong())).thenReturn(insiteSale);
        when(saleTaxRepository.findAllBySaleId(anyLong())).thenReturn(taxes);
        when(saleProductRepository.findAllBySaleId(anyLong())).thenReturn(products);

        if (insite) {
            List<InsiteSaleTable> tables = TestUtils.castList(
                    InsiteSaleTable.class,
                    Mockito.mock(List.class));

            when(insiteSaleTableRepository.findAllByInsiteSaleId(anyLong())).thenReturn(tables);
        }

        return SaleInfoDTO.builder()
                .sale(saleDTO)
                .insite(insite)
                .reservationId(insite ? null : insiteSale.get().getReservation().getId())
                .taxes(TestUtils.castList(TaxDTO.class, Mockito.mock(List.class)))
                .tables(insite
                        ? TestUtils.castList(TableDTO.class, Mockito.mock(List.class))
                        : new ArrayList<>())
                .products(TestUtils.castList(SaleProductDTO.class, Mockito.mock(List.class)))
                .build();
    }

    @Test
    void shouldSaveInsiteSale() {
        Boolean insite = true;
        Branch branch = utils.createBranch(null);
        ClientGuest clientGuest = utils.createClientGuest((Guest) null);
        Sale sale = utils.createSale(branch, clientGuest, null);
        SaleDTO saleDTO = utils.createSaleDTO(sale);
        Reservation reservation = utils.createReservation(branch);
        List<Tax> defaultTaxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(clientGuestRepository.findById(anyLong())).thenReturn(Optional.of(clientGuest));
        when(saleMapper.toEntity(any(SaleDTO.class), any(Branch.class), isNull(), any(ClientGuest.class)))
                .thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(taxRepository.findAllByBranchId(anyLong())).thenReturn(defaultTaxes);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        completeData(sale, insite);

        SaleInfoDTO dto = new SaleInfoDTO(
                saleDTO,
                insite,
                reservation.getId(),
                null,
                new ArrayList<>(),
                null);
        SaleInfoDTO response = saleService.save(dto);
        SaleInfoDTO expected = completeData(sale, insite);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldSaveOnlineSale() {
        Boolean insite = false;
        Sale sale = utils.createSale(null, null, null);
        SaleDTO saleDTO = utils.createSaleDTO(sale);
        Branch branch = utils.createBranch(null);
        ClientGuest clientGuest = utils.createClientGuest((Guest) null);
        List<Tax> defaultTaxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(clientGuestRepository.findById(anyLong())).thenReturn(Optional.of(clientGuest));
        when(saleMapper.toEntity(any(SaleDTO.class), any(Branch.class), isNull(), any(ClientGuest.class)))
                .thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(taxRepository.findAllByBranchId(anyLong())).thenReturn(defaultTaxes);
        completeData(sale, insite);

        SaleInfoDTO dto = new SaleInfoDTO(
                saleDTO,
                insite,
                null,
                null,
                new ArrayList<>(),
                null);
        SaleInfoDTO response = saleService.save(dto);
        SaleInfoDTO expected = completeData(sale, insite);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInSave() {
        SaleDTO saleDTO = utils.createSaleDTO(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());

        SaleInfoDTO dto = SaleInfoDTO.builder().sale(saleDTO).build();

        try {
            saleService.save(dto);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Branch with id " + saleDTO.getBranchId() + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingClientGuestInSave() {
        Branch branch = utils.createBranch(null);
        SaleDTO saleDTO = utils.createSaleDTO(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(clientGuestRepository.findById(anyLong())).thenReturn(Optional.empty());

        SaleInfoDTO dto = SaleInfoDTO.builder().sale(saleDTO).build();

        try {
            saleService.save(dto);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Client guest with id " + saleDTO.getClientGuestId() + " does not exists",
                    e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 59);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingReservationInSaveInsite() {
        Boolean insite = true;
        Branch branch = utils.createBranch(null);
        ClientGuest clientGuest = utils.createClientGuest((Guest) null);
        Sale sale = utils.createSale(branch, clientGuest, null);
        SaleDTO saleDTO = utils.createSaleDTO(sale);
        List<Tax> defaultTaxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(clientGuestRepository.findById(anyLong())).thenReturn(Optional.of(clientGuest));
        when(saleMapper.toEntity(any(SaleDTO.class), any(Branch.class), isNull(), any(ClientGuest.class)))
                .thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(taxRepository.findAllByBranchId(anyLong())).thenReturn(defaultTaxes);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        SaleInfoDTO dto = new SaleInfoDTO(
                saleDTO,
                insite,
                1L,
                null,
                new ArrayList<>(),
                null);

        try {
            saleService.save(dto);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Reservation with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 27);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingTableInSaveInsite() {
        Boolean insite = true;
        Branch branch = utils.createBranch(null);
        ClientGuest clientGuest = utils.createClientGuest((Guest) null);
        Sale sale = utils.createSale(branch, clientGuest, null);
        SaleDTO saleDTO = utils.createSaleDTO(sale);
        Reservation reservation = utils.createReservation(branch);
        TableDTO table = utils.createTableDTO(null);
        List<Tax> defaultTaxes = TestUtils.castList(Tax.class, Mockito.mock(List.class));

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(clientGuestRepository.findById(anyLong())).thenReturn(Optional.of(clientGuest));
        when(saleMapper.toEntity(any(SaleDTO.class), any(Branch.class), isNull(), any(ClientGuest.class)))
                .thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(taxRepository.findAllByBranchId(anyLong())).thenReturn(defaultTaxes);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(tableRepository.findById(anyLong())).thenReturn(Optional.empty());

        SaleInfoDTO dto = new SaleInfoDTO(
                saleDTO,
                insite,
                1L,
                null,
                List.of(table),
                null);

        try {
            saleService.save(dto);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Table with id " + table.getId() + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 49);
        }
    }

    @Test
    void shouldUpdate() {
        Sale sale = utils.createSale(null, null, null);
        SaleDTO saleDTO = utils.createSaleDTO(sale);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(saleMapper.updateModel(any(SaleDTO.class), any(Sale.class))).thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        SaleInfoDTO response = saleService.update(sale.getId(), saleDTO);
        SaleInfoDTO expected = completeData(sale, true);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldGetNoContentDueToSaleNotExistingInUpdate() {
        SaleDTO saleDTO = utils.createSaleDTO(null);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleService.update(1L, saleDTO);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldGetBadRequestDueToInvalidStatusInUpdate() {
        Sale sale = utils.createSale(null, null, null);
        SaleDTO saleDTO = utils.createSaleDTO(sale);

        sale.setStatus(SaleStatics.Status.CLOSED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.update(sale.getId(), saleDTO);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + sale.getId() + " is closed", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 43);
        }

        sale.setStatus(SaleStatics.Status.CANCELLED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.update(sale.getId(), saleDTO);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + sale.getId() + " is cancelled", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 48);
        }
    }

    @Test
    void shouldAddTax() {
        Tax tax = utils.createTax();
        TaxDTO taxDTO = utils.createTaxDTO(tax);
        Sale sale = utils.createSale(null, null, null);
        SaleTax saleTax = utils.createSaleTax(sale, tax);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        when(taxMapper.toEntity(any(TaxDTO.class))).thenReturn(tax);
        when(taxRepository.save(any(Tax.class))).thenReturn(tax);
        when(saleTaxRepository.save(any(SaleTax.class))).thenReturn(saleTax);
        when(taxMapper.toDTO(any(Tax.class))).thenReturn(taxDTO);

        TaxDTO response = saleService.addTax(sale.getId(), taxDTO);

        assertThat(response).isEqualTo(taxDTO);
    }

    @Test
    void shouldGetNoContentDueToMissingSaleInAddTax() {
        Tax tax = utils.createTax();
        TaxDTO taxDTO = utils.createTaxDTO(tax);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleService.addTax(1L, taxDTO);
        } catch (NoContentException e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals("Sale with id " + 1L + " does not exists", e.getMessage());
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 42);
        }
    }

    @Test
    void shouldGetBadRequestDueToInvalidStatusInAddTax() {
        Tax tax = utils.createTax();
        TaxDTO taxDTO = utils.createTaxDTO(tax);
        Sale sale = utils.createSale(null, null, null);

        sale.setStatus(SaleStatics.Status.CLOSED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.addTax(sale.getId(), taxDTO);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + sale.getId() + " is closed", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 43);
        }

        sale.setStatus(SaleStatics.Status.CANCELLED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.addTax(sale.getId(), taxDTO);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + sale.getId() + " is cancelled", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 48);
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
    void shouldGetNoContentDueToMissingSaleInDelete(){
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
    void shouldGetBadRequestDueToInvalidStatusInDelete() {
        Sale sale = utils.createSale(null, null, null);

        sale.setStatus(SaleStatics.Status.CLOSED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.delete(sale.getId());
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + sale.getId() + " is closed", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 43);
        }

        sale.setStatus(SaleStatics.Status.CANCELLED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.delete(sale.getId());
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + sale.getId() + " is cancelled", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 48);
        }
    }

    @Test
    void shouldClearSaleProducts() {
        Sale sale = utils.createSale(null, null, null);

        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        saleService.clearSaleProducts(sale.getId());

        verify(saleProductRepository, times(1)).deleteAllBySaleId(sale.getId());
    }

    @Test
    void shouldGetNoContentDueToMissingSaleInClearSaleProducts(){
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
    void shouldGetBadRequestDueToInvalidStatusInClearSaleProducts() {
        Sale sale = utils.createSale(null, null, null);

        sale.setStatus(SaleStatics.Status.CLOSED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.clearSaleProducts(1L);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is closed", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 43);
        }

        sale.setStatus(SaleStatics.Status.CANCELLED);
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));

        try {
            saleService.clearSaleProducts(1L);
        } catch (BadRequestException e) {
            Assert.assertTrue(e instanceof BadRequestException);
            Assert.assertEquals("Sale with id " + 1L + " is cancelled", e.getMessage());
            Assert.assertEquals(((BadRequestException) e).getCode(), (Integer) 48);
        }
    }

    @Test
    void shouldGetBranchSales() {
        Branch branch = utils.createBranch(null);

        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));

        List<Sale> sales = new ArrayList<>();

        when(saleRepository.findAllByBranchIdAndFilters(anyLong(), any(), any(), any(), any(), any(), any()))
                .thenReturn(sales);
        when(saleRepository.findAllByBranchIdAndStatusOrderByStartTimeDesc(
                anyLong(), any())).thenReturn(sales);

        BranchSalesInfoDTO response = saleService.getBranchSales(
                1,
                10,
                branch.getId(),
                new Date(),
                new Date(),
                "fullname test",
                "identity document test");

        assertThat(response).isNotNull();

    }

    @Test
    void shouldGetNoContentDueToBranchNotExistingInGetBranchSales(){
        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            saleService.getBranchSales(1, 10, 1L, null, null, null, null);
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
            saleService.getBranchSales(-1, 10, 1L, null, null, null, null);
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
            saleService.getBranchSales(1, 0, 1L, null, null, null, null);
        } catch (UnprocessableException e) {
            Assert.assertTrue(e instanceof UnprocessableException);
            Assert.assertEquals("Page size cannot be less than one", e.getMessage());
            Assert.assertEquals(((UnprocessableException) e).getCode(), (Integer) 45);
        }
    }
}

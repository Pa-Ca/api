package com.paca.paca.sale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Table;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.PaymentOptionRepository;
import com.paca.paca.branch.repository.TableRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.repository.ReservationRepository;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.sale.statics.SaleStatics;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SaleRepositoryTest extends PacaTest {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentOptionRepository paymentOptionRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .reservationRepository(reservationRepository)
                .businessRepository(businessRepository)
                .branchRepository(branchRepository)
                .paymentOptionRepository(paymentOptionRepository)
                .tableRepository(tableRepository)
                .saleRepository(saleRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        saleRepository.deleteAll();
        tableRepository.deleteAll();
        paymentOptionRepository.deleteAll();
        reservationRepository.deleteAll();
        branchRepository.deleteAll();
        
    }

    @AfterEach
    void restoreTest() {
        reservationRepository.deleteAll();
        saleRepository.deleteAll();
        tableRepository.deleteAll();
        paymentOptionRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
        businessRepository.deleteAll();
        branchRepository.deleteAll();

    }

    @Test
    void shouldCreateSale() {
        // Arrange
        Table table = utils.createTable(null);
        Sale sale = Sale.builder()
                .table(table)
                .startTime(new Date(System.currentTimeMillis()))
                .status(SaleStatics.Status.ongoing)
                .dollarToLocalCurrencyExchange(BigDecimal.valueOf(25.0))
                .note("Nota de prueba")
                .reservation(null)
                .tableName(table.getName())
                .clientQuantity(2)
                .endTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2))
                .build();

        // Act
        Sale savedSale = saleRepository.save(sale);

        // Assert
        assertThat(savedSale).isNotNull();
        assertThat(savedSale.getId()).isEqualTo(sale.getId());
        assertThat(savedSale.getStartTime()).isEqualTo(sale.getStartTime());
        assertThat(savedSale.getStatus()).isEqualTo(sale.getStatus());
        assertThat(savedSale.getDollarToLocalCurrencyExchange()).isEqualTo(sale.getDollarToLocalCurrencyExchange());
        assertThat(savedSale.getNote()).isEqualTo(sale.getNote());
        assertThat(savedSale.getReservation()).isEqualTo(sale.getReservation());
        assertThat(savedSale.getClientQuantity()).isEqualTo(sale.getClientQuantity());
        assertThat(savedSale.getEndTime()).isEqualTo(sale.getEndTime());
        assertThat(savedSale.getTableName()).isEqualTo(sale.getTableName());
        // Check that the table is the same
        assertThat(savedSale.getTable()).isEqualTo(table);

    }

    @Test
    void shouldGetAllSales() {
        // Arrange
        Branch branchA = utils.createBranch(null);
        Table tableAA = utils.createTable(branchA);
        Table tableAB = utils.createTable(branchA);

        Branch branchB = utils.createBranch(null);
        Table tableBA = utils.createTable(branchB);

        Reservation reservation = utils.createReservation(branchB);

        Sale sale1 = Sale.builder()
                .table(tableAA)
                .tableName(tableAA.getName())
                .note("Nota de prueba 1")
                .startTime(new Date(System.currentTimeMillis()))
                .clientQuantity(2)
                .dollarToLocalCurrencyExchange(BigDecimal.valueOf(20.0))
                .reservation(null)
                .status(SaleStatics.Status.ongoing)
                .tableName(tableAA.getName())
                .build();
        Sale sale2 = Sale.builder()
                .table(tableBA)
                .tableName(tableBA.getName())
                .note("Nota de prueba 2")
                .startTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .clientQuantity(2)
                .dollarToLocalCurrencyExchange(BigDecimal.valueOf(23.0))
                .status(SaleStatics.Status.ongoing)
                .reservation(null)
                .tableName(tableBA.getName())
                .build();
        Sale sale3 = Sale.builder()
                .table(tableAB)
                .tableName(tableAB.getName())
                .startTime(new Date(System.currentTimeMillis() + 1100 * 60 * 60 * 24 * 2))
                .note("Nota de prueba 3")
                .reservation(reservation)
                .clientQuantity(2)
                .status(SaleStatics.Status.closed)
                .tableName(tableAB.getName())
                .endTime(new Date(System.currentTimeMillis() + 1200 * 60 * 60 * 24 * 2))
                .dollarToLocalCurrencyExchange(BigDecimal.valueOf(25.0))
                .build();

        saleRepository.save(sale1);
        saleRepository.save(sale2);
        saleRepository.save(sale3);

        // Act
        List<Sale> sales = saleRepository.findAll();

        // Assert
        assertThat(sales).isNotNull();
        assertThat(sales.size()).isEqualTo(3);
        assertThat(sales.get(0)).isEqualTo(sale1);
        assertThat(sales.get(1)).isEqualTo(sale2);
        assertThat(sales.get(2)).isEqualTo(sale3);
    }

    @Test
    void shouldGetAllSalesByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual() {
        // This is to test the query
        // findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual

        // Create two branches
        Branch branchA = utils.createBranch(null);
        Branch branchB = utils.createBranch(null);

        List<Sale> sales = utils.createTestSales(branchA, branchB);

        // Get the times the will be used to test the method
        Calendar calendar = Calendar.getInstance();
        // Get now
        // Date now = calendar.getTime();
        // // Get an hour after now
        // calendar.add(Calendar.HOUR_OF_DAY, 1);
        // Date oneHourAfterNow = calendar.getTime();
        // // Get two hours after now
        // calendar.add(Calendar.HOUR_OF_DAY, 1);
        // Date twoHoursAfterNow = calendar.getTime();
        // // Get three hours after now
        // calendar.add(Calendar.HOUR_OF_DAY, 1);
        // Date threeHoursAfterNow = calendar.getTime();
        // // Get one hour before now
        // calendar.add(Calendar.HOUR_OF_DAY, -4);
        // Date oneHourBeforeNow = calendar.getTime();
        // // Get three hours before now
        // calendar.add(Calendar.HOUR_OF_DAY, -5);
        // Date twoHoursBeforeNow = calendar.getTime();
        // // Get a year after now
        // calendar.add(Calendar.YEAR, 1);
        // Date oneYearAfterNow = calendar.getTime();
        // Get a year before now
        calendar.add(Calendar.YEAR, -2);
        Date oneYearBeforeNow = calendar.getTime();

        // We first have to create the page request to test the method
        Pageable test_page;

        test_page = PageRequest.of(
                0,
                10,
                Sort.by("startTime").descending());

        // Now create a list of sales from the branch A that are ongoing from now
        // You have to do it manually because the repository doesn't have a method for
        // that
        List<Sale> ongoingSalesFromBranchA = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) &&
                    sale.getStatus().equals(SaleStatics.Status.ongoing) &&
                    sale.getStartTime().compareTo(oneYearBeforeNow) >= 0)

            {
                ongoingSalesFromBranchA.add(sale);
            }
        }

        // Get the ongoing sales from branch A
        List<Sale> ongoingSalesFromBranchAFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                        branchA.getId(),
                        List.of(SaleStatics.Status.ongoing),
                        oneYearBeforeNow,
                        test_page)
                .getContent();

        // Compare the lists lengths
        assertEquals(ongoingSalesFromBranchA.size(), ongoingSalesFromBranchAFromRepositoryList.size());

        // Now create a list of sales from the branch A that are closed from now
        // You have to do it manually because the repository doesn't have a method for
        // that
        List<Sale> closedSalesFromBranchA = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) &&
                    sale.getStatus().equals(SaleStatics.Status.closed) &&
                    sale.getStartTime().compareTo(oneYearBeforeNow) >= 0) {
                closedSalesFromBranchA.add(sale);
            }
        }

        // Now repeat the process with the closed sales from branch A
        List<Sale> closedSalesFromBranchAFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                        branchA.getId(),
                        List.of(SaleStatics.Status.closed),
                        oneYearBeforeNow,
                        test_page)
                .getContent();

        // Compare the lists lengths
        assertEquals(closedSalesFromBranchA.size(), closedSalesFromBranchAFromRepositoryList.size());

        // Get the cancelled sales from branch A
        List<Sale> cancelledSalesFromBranchA = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) &&
                    sale.getStatus().equals(SaleStatics.Status.cancelled) &&
                    sale.getStartTime().compareTo(oneYearBeforeNow) >= 0) {
                cancelledSalesFromBranchA.add(sale);
            }
        }

        // Now repeat the process with the cancelled sales from branch A
        List<Sale> cancelledSalesFromBranchAFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                        branchA.getId(),
                        List.of(SaleStatics.Status.cancelled),
                        oneYearBeforeNow,
                        test_page)
                .getContent();

        // Compare the lists lengths
        assertEquals(cancelledSalesFromBranchA.size(), cancelledSalesFromBranchAFromRepositoryList.size());

        // Now do the same but with branch B
        List<Sale> ongoingSalesFromBranchB = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchB) &&
                    sale.getStatus().equals(SaleStatics.Status.ongoing) &&
                    sale.getStartTime().compareTo(oneYearBeforeNow) >= 0) {
                ongoingSalesFromBranchB.add(sale);
            }
        }

        // Now repeat the process with the ongoing sales from branch B

        List<Sale> ongoingSalesFromBranchBFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                        branchB.getId(),
                        List.of(SaleStatics.Status.ongoing),
                        oneYearBeforeNow,
                        test_page)
                .getContent();

        // Compare the lists lengths

        assertEquals(ongoingSalesFromBranchB.size(), ongoingSalesFromBranchBFromRepositoryList.size());

        List<Sale> closedSalesFromBranchB = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchB) &&
                    sale.getStatus().equals(SaleStatics.Status.closed) &&
                    sale.getStartTime().compareTo(oneYearBeforeNow) >= 0) {
                closedSalesFromBranchB.add(sale);
            }
        }

        // Now repeat the process with the closed sales from branch B

        List<Sale> closedSalesFromBranchBFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                        branchB.getId(),
                        List.of(SaleStatics.Status.closed),
                        oneYearBeforeNow,
                        test_page)
                .getContent();

        // Compare the lists lengths

        assertEquals(closedSalesFromBranchB.size(), closedSalesFromBranchBFromRepositoryList.size());

        // Get the cancelled sales from branch B
        List<Sale> cancelledSalesFromBranchB = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchB) &&
                    sale.getStatus().equals(SaleStatics.Status.cancelled) &&
                    sale.getStartTime().compareTo(oneYearBeforeNow) >= 0) {
                cancelledSalesFromBranchB.add(sale);
            }
        }

        // Now repeat the process with the cancelled sales from branch B

        List<Sale> cancelledSalesFromBranchBFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqual(
                        branchB.getId(),
                        List.of(SaleStatics.Status.cancelled),
                        oneYearBeforeNow,
                        test_page)
                .getContent();

        assertEquals(cancelledSalesFromBranchB.size(), cancelledSalesFromBranchBFromRepositoryList.size());

    }

    @Test
    void shouldFindAllByTableBranchIdAndStatusOrderByStartTimeDesc() {
        // Create the branch A and B
        Branch branchA = utils.createBranch(null);
        Branch branchB = utils.createBranch(null);
        // Create the test sales
        List<Sale> sales = utils.createTestSales(branchA, branchB);

        // Maually create the lists of sales from branch A that are ongoing, and order
        // them by start time descending
        List<Sale> ongoingSalesFromBranchA = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) &&
                    sale.getStatus().equals(SaleStatics.Status.ongoing)) {
                ongoingSalesFromBranchA.add(sale);
            }
        }

        ongoingSalesFromBranchA.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));

        // Now query the repository for the same list
        List<Sale> ongoingSalesFromBranchAFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusOrderByStartTimeDesc(
                        branchA.getId(),
                        SaleStatics.Status.ongoing);

        // Comparte the lists lenghts
        assertEquals(ongoingSalesFromBranchA.size(), ongoingSalesFromBranchAFromRepositoryList.size());
        // Compare the lists elements
        for (int i = 0; i < ongoingSalesFromBranchA.size(); i++) {
            assertEquals(ongoingSalesFromBranchA.get(i), ongoingSalesFromBranchAFromRepositoryList.get(i));
        }

        // Do the same with the closed sales from branch A
        List<Sale> closedSalesFromBranchA = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) &&
                    sale.getStatus().equals(SaleStatics.Status.closed)) {
                closedSalesFromBranchA.add(sale);
            }
        }

        closedSalesFromBranchA.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));

        // Now query the repository for the same list

        List<Sale> closedSalesFromBranchAFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusOrderByStartTimeDesc(
                        branchA.getId(),
                        SaleStatics.Status.closed);

        // Comparte the lists lenghts
        assertEquals(closedSalesFromBranchA.size(), closedSalesFromBranchAFromRepositoryList.size());
        // Compare the lists elements
        for (int i = 0; i < closedSalesFromBranchA.size(); i++) {
            assertEquals(closedSalesFromBranchA.get(i), closedSalesFromBranchAFromRepositoryList.get(i));
        }

        // Now do the same for the ongoing sales from branch B
        List<Sale> ongoingSalesFromBranchB = new ArrayList<>();

        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchB) &&
                    sale.getStatus().equals(SaleStatics.Status.ongoing)) {
                ongoingSalesFromBranchB.add(sale);
            }
        }

        ongoingSalesFromBranchB.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));

        // Now query the repository for the same list

        List<Sale> ongoingSalesFromBranchBFromRepositoryList = saleRepository
                .findAllByTableBranchIdAndStatusOrderByStartTimeDesc(
                        branchB.getId(),
                        SaleStatics.Status.ongoing);

        // Comparte the lists lenghts

        assertEquals(ongoingSalesFromBranchB.size(), ongoingSalesFromBranchBFromRepositoryList.size());

        // Compare the lists elements

        for (int i = 0; i < ongoingSalesFromBranchB.size(); i++) {
            assertEquals(ongoingSalesFromBranchB.get(i), ongoingSalesFromBranchBFromRepositoryList.get(i));
        }

    }
}

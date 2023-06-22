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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
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

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .reservationRepository(reservationRepository)
                .businessRepository(businessRepository)
                .branchRepository(branchRepository)
                .tableRepository(tableRepository)
                .saleRepository(saleRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        saleRepository.deleteAll();
        tableRepository.deleteAll();
        reservationRepository.deleteAll();
        branchRepository.deleteAll();        
    }

    @AfterEach
    void restoreTest() {
        reservationRepository.deleteAll();
        saleRepository.deleteAll();
        tableRepository.deleteAll();
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
    void shouldGetAllSales(){
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
    void shouldGetAllSalesByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(){
        // This is to test the query findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual

        // Create two branches
        Branch branchA = utils.createBranch(null);
        Branch branchB = utils.createBranch(null);

        List<Sale>  sales = utils.createTestSales(branchA, branchB);
        
        // Now create a list of sales from the branch A that are ongoing from now
        // You have to do it manually because the repository doesn't have a method for that 
        List<Sale> ongoingSalesFromBranchA = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) && 
                sale.getStatus().equals(SaleStatics.Status.ongoing)) 
            {
                ongoingSalesFromBranchA.add(sale);
            }
        }

        // Now create a list of sales from the branch A that are closed from now
        // You have to do it manually because the repository doesn't have a method for that
        List<Sale> closedSalesFromBranchA = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) && 
                sale.getStatus().equals(SaleStatics.Status.closed)) 
            {
                closedSalesFromBranchA.add(sale);
            }
        }

        // Get the cancelled sales from branch A
        List<Sale> cancelledSalesFromBranchA = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchA) && 
                sale.getStatus().equals(SaleStatics.Status.cancelled)) 
            {
                cancelledSalesFromBranchA.add(sale);
            }
        }

        // Now do the same but with branch B
        List<Sale> ongoingSalesFromBranchB = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchB) && 
                sale.getStatus().equals(SaleStatics.Status.ongoing)) 
            {
                ongoingSalesFromBranchB.add(sale);
            }
        }

        List<Sale> closedSalesFromBranchB = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchB) && 
                sale.getStatus().equals(SaleStatics.Status.closed)) 
            {
                closedSalesFromBranchB.add(sale);
            }
        }

        // Get the cancelled sales from branch B
        List<Sale> cancelledSalesFromBranchB = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getTable().getBranch().equals(branchB) && 
                sale.getStatus().equals(SaleStatics.Status.cancelled)) 
            {
                cancelledSalesFromBranchB.add(sale);
            }
        }

        // Now lets test the repository method 
        // Page<Sale> findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual

        // We first have to create the page request
        Pageable test_page;
                
        test_page = PageRequest.of(
                0,
                10,
                Sort.by("startTime").descending());

        // Now we can test the method

        // Get the ongoing sales from branch A
        Page<Sale> ongoingSalesFromBranchAFromRepository = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branchA.getId(),
                List.of(SaleStatics.Status.ongoing),
                new Date(0),
                new Date(Integer.MAX_VALUE -1),
                test_page);
        // Get the list from the page
        List<Sale> ongoingSalesFromBranchAFromRepositoryList = ongoingSalesFromBranchAFromRepository.getContent();

        // Compare the lists lengths
        assertEquals(ongoingSalesFromBranchA.size(), ongoingSalesFromBranchAFromRepositoryList.size());

        // Now repeat the process with the closed sales from branch A
        Page<Sale> closedSalesFromBranchAFromRepository = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branchA.getId(),
                List.of(SaleStatics.Status.closed),
                new Date(0),
                new Date(Long.MAX_VALUE),
                test_page);
        
        List<Sale> closedSalesFromBranchAFromRepositoryList = closedSalesFromBranchAFromRepository.getContent();

        // Compare the lists lengths
        assertEquals(closedSalesFromBranchA.size(), closedSalesFromBranchAFromRepositoryList.size());

        // Now repeat the process with the cancelled sales from branch A
        Page<Sale> cancelledSalesFromBranchAFromRepository = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branchA.getId(),
                List.of(SaleStatics.Status.cancelled),
                new Date(0),
                new Date(Long.MAX_VALUE),
                test_page);
        
        List<Sale> cancelledSalesFromBranchAFromRepositoryList = cancelledSalesFromBranchAFromRepository.getContent();

        // Compare the lists lengths
        assertEquals(cancelledSalesFromBranchA.size(), cancelledSalesFromBranchAFromRepositoryList.size());

        // Now repeat the process with the ongoing sales from branch B

        Page<Sale> ongoingSalesFromBranchBFromRepository = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branchB.getId(),
                List.of(SaleStatics.Status.ongoing),
                new Date(0),
                new Date(Long.MAX_VALUE),
                test_page);

        List<Sale> ongoingSalesFromBranchBFromRepositoryList = ongoingSalesFromBranchBFromRepository.getContent();

        // Compare the lists lengths

        assertEquals(ongoingSalesFromBranchB.size(), ongoingSalesFromBranchBFromRepositoryList.size());

        // Now repeat the process with the closed sales from branch B

        Page<Sale> closedSalesFromBranchBFromRepository = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branchB.getId(),
                List.of(SaleStatics.Status.closed),
                new Date(0),
                new Date(Long.MAX_VALUE),
                test_page);

        List<Sale> closedSalesFromBranchBFromRepositoryList = closedSalesFromBranchBFromRepository.getContent();

        // Compare the lists lengths

        assertEquals(closedSalesFromBranchB.size(), closedSalesFromBranchBFromRepositoryList.size());

        // Now repeat the process with the cancelled sales from branch B

        Page<Sale> cancelledSalesFromBranchBFromRepository = saleRepository.findAllByTableBranchIdAndStatusInAndStartTimeGreaterThanEqualAndEndTimeLessThanEqual(
                branchB.getId(),
                List.of(SaleStatics.Status.cancelled),
                new Date(0),
                new Date(Long.MAX_VALUE),
                test_page);

        List<Sale> cancelledSalesFromBranchBFromRepositoryList = cancelledSalesFromBranchBFromRepository.getContent();

        // Compare the lists lengths

        assertEquals(cancelledSalesFromBranchB.size(), cancelledSalesFromBranchBFromRepositoryList.size());

        
    }
}

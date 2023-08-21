package com.paca.paca.sale;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.sale.model.Sale;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.sale.statics.SaleStatics;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaleRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllSalesByTableBranchIdAndFilters() {
        Random rand = new Random();

        // Create random branches
        List<Branch> branches = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            branches.add(utils.createBranch(null));
        }

        // Create random names
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            names.add("name_" + i + " ");
        }

        // Create random surnames
        List<String> surnames = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            surnames.add("surname_" + i + " ");
        }

        // Create random identity documents
        List<String> identityDocuments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            identityDocuments.add("V" + i);
        }

        // Min and max dates
        Date minDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(minDate);
        calendar.add(Calendar.MONTH, 1);
        Date maxDate = calendar.getTime();

        List<Sale> sales = utils.createTestSales(
                branches,
                minDate,
                maxDate,
                names,
                surnames,
                identityDocuments);

        // Select a random values
        Branch branch = branches.get(rand.nextInt(branches.size()));
        Date startDate = new Date(ThreadLocalRandom.current().nextLong(
                minDate.getTime(),
                maxDate.getTime()));
        Date endDate = new Date(ThreadLocalRandom.current().nextLong(
                startDate.getTime(),
                maxDate.getTime()));
        Short status = SaleStatics.Status.ALL.get(rand.nextInt(SaleStatics.Status.ALL.size()));
        String name = names.get(rand.nextInt(names.size()));
        String surname = surnames.get(rand.nextInt(surnames.size()));
        String identityDocument = identityDocuments.get(rand.nextInt(identityDocuments.size()));

        // Filter the sales
        List<Sale> expected = sales.stream()
                .filter(sale -> sale.getBranch().getId().equals(branch.getId()))
                .filter(sale -> sale.getStartTime().compareTo(startDate) >= 0)
                .filter(sale -> sale.getStartTime().compareTo(endDate) <= 0)
                .filter(sale -> sale.getStatus().equals(status))
                .filter(sale -> {
                    if (sale.getClientGuest().getHaveGuest()) {
                        return sale.getClientGuest().getGuest().getName().contains(name)
                                && sale.getClientGuest().getGuest().getSurname().contains(surname)
                                && sale.getClientGuest().getGuest().getIdentityDocument().contains(identityDocument);
                    } else {
                        return sale.getClientGuest().getClient().getName().contains(name)
                                && sale.getClientGuest().getClient().getSurname().contains(surname)
                                && sale.getClientGuest().getClient().getIdentityDocument().contains(identityDocument);
                    }
                })
                .collect(Collectors.toList());

        // Get the sales from the repository
        List<Sale> response = saleRepository.findAllByBranchIdAndFilters(
                branch.getId(),
                List.of(status),
                startDate,
                endDate,
                name,
                surname,
                identityDocument);

        assertThat(response.size()).isEqualTo(expected.size());
    }

    @Test
    void shouldFindAllByBranchIdAndStatusOrderByStartTimeDesc() {
        int nSales = 10;
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < nSales; i++) {
            utils.createSale(null, null, null);

            Sale sale = utils.createSale(branch, null, null);
            sale.setStatus(SaleStatics.Status.CLOSED);
            saleRepository.save(sale);

            sale = utils.createSale(branch, null, null);
            sale.setStatus(SaleStatics.Status.ONGOING);
            saleRepository.save(sale);
        }

        List<Sale> sales = saleRepository.findAllByBranchIdAndStatusOrderByStartTimeDesc(
                branch.getId(),
                SaleStatics.Status.ONGOING);

        assertEquals(sales.size(), nSales);
    }

    @Test
    void shouldCheckThatExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        Sale sale = utils.createSale(branch, null, null);

        boolean exists = saleRepository.existsByIdAndBranch_Business_Id(
                sale.getId(),
                branch.getBusiness().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckThatDoesNotExistsByIdAndBranch_Business_Id() {
        Branch branch = utils.createBranch(null);
        Sale sale = utils.createSale(branch, null, null);

        boolean exists = saleRepository.existsByIdAndBranch_Business_Id(
                sale.getId(),
                branch.getBusiness().getId() + 1);

        assertThat(exists).isFalse();
    }
}

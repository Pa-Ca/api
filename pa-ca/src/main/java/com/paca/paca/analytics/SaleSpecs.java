package com.paca.paca.analytics;

import com.paca.paca.sale.model.Sale;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SaleSpecs {
    public static Specification<Sale> salesLastDay() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return (sale, query, builder) -> builder.greaterThanOrEqualTo(sale.get("startTime"), yesterday);
    }

    public static Specification<Sale> salesLastWeek() {
        LocalDate lastWeek = LocalDate.now().minusWeeks(1);
        return (sale, query, builder) -> builder.greaterThanOrEqualTo(sale.get("startTime"), lastWeek);
    }

    public static Specification<Sale> salesLastMonth() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        return (sale, query, builder) -> builder.greaterThanOrEqualTo(sale.get("startTime"), lastMonth);
    }

    public static Specification<Sale> salesLastYear() {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        return (sale, query, builder) -> builder.greaterThanOrEqualTo(sale.get("startTime"), lastYear);
    }
}

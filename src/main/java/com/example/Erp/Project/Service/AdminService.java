package com.example.Erp.Project.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.Erp.Project.DTO.AdminDashboardDTO;
import com.example.Erp.Project.Entity.DashboardFilter;
import com.example.Erp.Project.Repository.InvoiceRepository;
import com.example.Erp.Project.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    // ==========================
    // DEFAULT DASHBOARD (FIX ✅)
    // ==========================
    public AdminDashboardDTO getDashboardSummary() {
        return getDashboardSummary(DashboardFilter.THIS_MONTH);
    }

    // ==========================
    // DASHBOARD SUMMARY
    // ==========================
    public AdminDashboardDTO getDashboardSummary(DashboardFilter filter) {

        LocalDate[] range = resolveDateRange(filter);
        LocalDate from = range[0];
        LocalDate to = range[1];

        AdminDashboardDTO dto = new AdminDashboardDTO(
                null, null, null, null, null, null
        );

        dto.setTotalUsers(userRepository.count());

        dto.setTotalSales(
                Optional.ofNullable(
                        invoiceRepository.getTotalSales(from, to)
                ).orElse(java.math.BigDecimal.ZERO)
        );

        dto.setPendingInvoices(
                (long) invoiceRepository.findByStatus("UNPAID").size()
        );

        return dto;
    }

    // ==========================
    // SALES CHART
    // ==========================
    public List<Map<String, Object>> getSalesChart(LocalDate from, LocalDate to) {

        List<Object[]> rows =
                invoiceRepository.salesSummary(from, to);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("totalSales", row[1]);
            result.add(map);
        }

        return result;
    }

    // ==========================
    // DATE RANGE RESOLVER
    // ==========================
    private LocalDate[] resolveDateRange(DashboardFilter filter) {

        LocalDate now = LocalDate.now();

        switch (filter) {

            case TODAY:
                return new LocalDate[]{ now, now };

            case THIS_WEEK:
                return new LocalDate[]{
                        now.with(DayOfWeek.MONDAY),
                        now
                };

            case LAST_MONTH:
                LocalDate lastMonth = now.minusMonths(1);
                return new LocalDate[]{
                        lastMonth.withDayOfMonth(1),
                        lastMonth.withDayOfMonth(lastMonth.lengthOfMonth())
                };

            case THIS_MONTH:
            default:
                return new LocalDate[]{
                        now.withDayOfMonth(1),
                        now
                };
        }
    }
}

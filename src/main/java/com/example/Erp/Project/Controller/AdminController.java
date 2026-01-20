package com.example.Erp.Project.Controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.Erp.Project.DTO.AdminDashboardDTO;
import com.example.Erp.Project.Entity.DashboardFilter;
import com.example.Erp.Project.Service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // ==========================
    // DASHBOARD SUMMARY
    // ==========================
    @GetMapping("/dashboard")
    public AdminDashboardDTO dashboard(
            @RequestParam(defaultValue = "THIS_MONTH") DashboardFilter filter) {

        return adminService.getDashboardSummary(filter);
    }

    // ==========================
    // SALES CHART (FIXED ✅)
    // ==========================
    @GetMapping("/dashboard/sales-chart")
    public List<Map<String, Object>> salesChart(
            @RequestParam(defaultValue = "THIS_MONTH") DashboardFilter filter) {

        LocalDate[] range = resolveDateRange(filter);
        return adminService.getSalesChart(range[0], range[1]);
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

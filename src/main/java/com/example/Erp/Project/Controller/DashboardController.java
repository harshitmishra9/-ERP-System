package com.example.Erp.Project.Controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import com.example.Erp.Project.DTO.DashboardSummaryDTO;
import com.example.Erp.Project.Entity.Invoice;
import com.example.Erp.Project.Entity.Product;
import com.example.Erp.Project.Repository.InvoiceRepository;
import com.example.Erp.Project.Service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;
    private final InvoiceRepository invoiceRepo;

    // KPI SUMMARY
    @GetMapping("/summary")
    public DashboardSummaryDTO summary() {
        return service.getSummary();
    }

    // LOW STOCK ALERTS
    @GetMapping("/stock-alerts")
    public List<Product> lowStock() {
        return service.getLowStockProducts();
    }

    // UNPAID INVOICES
    @GetMapping("/pending-invoices")
    public List<Invoice> pendingInvoices() {
        return service.getPendingInvoices();
    }

    // ✅ SALES CHART API (FIXES FRONTEND ERROR)
    @GetMapping("/sales-summary")
    public List<Map<String, Object>> salesSummary(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {

        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }

        if (to == null) {
            to = LocalDate.now();
        }

        List<Object[]> rows = invoiceRepo.salesSummary(from, to);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : rows) {

            Map<String, Object> map = new HashMap<>();

            map.put("date", row[0]);
            map.put("totalSales", row[1]);

            result.add(map);
        }

        return result;
    }
}

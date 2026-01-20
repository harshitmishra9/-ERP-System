package com.example.Erp.Project.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.example.Erp.Project.Repository.UserRepository;


import org.springframework.stereotype.Service;

import com.example.Erp.Project.DTO.AdminDashboardDTO;
import com.example.Erp.Project.DTO.DashboardSummaryDTO;
import com.example.Erp.Project.Entity.Invoice;
import com.example.Erp.Project.Entity.Product;
import com.example.Erp.Project.Repository.InvoiceRepository;
import com.example.Erp.Project.Repository.ProductRepository;
import com.example.Erp.Project.Repository.PurchaseOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceRepository invoiceRepo;
    private final PurchaseOrderRepository poRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    // --------------------
    // USER DASHBOARD
    // --------------------

    @Override
    public DashboardSummaryDTO getSummary() {

        BigDecimal sales = invoiceRepo.totalSalesThisMonth();

        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = start.plusMonths(1);

        BigDecimal purchases =
                poRepo.totalPurchasesBetweenDates(start, end);

        Long pending =
                (long) invoiceRepo.findByStatus("UNPAID").size();

        return new DashboardSummaryDTO(
                sales,
                purchases,
                pending
        );
    }

    @Override
    public List<Product> getLowStockProducts() {
        return productRepo.findLowStock();
    }

    @Override
    public List<Invoice> getPendingInvoices() {
        return invoiceRepo.findByStatus("UNPAID");
    }

    // --------------------
    // ADMIN DASHBOARD
    // --------------------

 // --------------------
 // ADMIN DASHBOARD
 // --------------------

 @Override
 public AdminDashboardDTO getAdminSummary() {

     BigDecimal sales = invoiceRepo.totalSalesThisMonth();

     LocalDate start = LocalDate.now().withDayOfMonth(1);
     LocalDate end = start.plusMonths(1);

     BigDecimal purchases =
             poRepo.totalPurchasesBetweenDates(start, end);

     BigDecimal profit = sales.subtract(purchases);

     Long pendingInvoices =
             (long) invoiceRepo.findByStatus("UNPAID").size();

     Long lowStockItems =
             (long) productRepo.findLowStock().size();

     // ✅ FIX: TOTAL USERS
     Long totalUsers = userRepo.count();

     return new AdminDashboardDTO(
             sales,
             purchases,
             profit,
             pendingInvoices,
             lowStockItems,
             totalUsers
     );
 }

    }


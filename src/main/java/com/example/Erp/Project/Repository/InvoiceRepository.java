package com.example.Erp.Project.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Erp.Project.Entity.Invoice;
import com.example.Erp.Project.Entity.SalesOrder;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // ========================
    // SALES ORDER CHECK
    // ========================
    boolean existsBySalesOrder(SalesOrder salesOrder);

    Optional<Invoice> findBySalesOrder(SalesOrder salesOrder);

    // ========================
    // TOTAL SALES THIS MONTH
    // ========================
    @Query("""
        SELECT COALESCE(SUM(i.totalPayable), 0)
        FROM Invoice i
        WHERE MONTH(i.createdDate) = MONTH(CURRENT_DATE)
          AND YEAR(i.createdDate) = YEAR(CURRENT_DATE)
    """)
    BigDecimal totalSalesThisMonth();

    // ========================
    // TOTAL SALES BETWEEN DATES (ADMIN DASHBOARD FIX ✅)
    // ========================
    @Query("""
        SELECT COALESCE(SUM(i.totalPayable), 0)
        FROM Invoice i
        WHERE i.createdDate BETWEEN :from AND :to
    """)
    BigDecimal getTotalSales(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    // ========================
    // SALES SUMMARY FOR CHART
    // ========================
    @Query("""
        SELECT i.createdDate, SUM(i.totalPayable)
        FROM Invoice i
        WHERE i.createdDate BETWEEN :from AND :to
        GROUP BY i.createdDate
        ORDER BY i.createdDate
    """)
    List<Object[]> salesSummary(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    // ========================
    // PENDING INVOICES
    // ========================
    List<Invoice> findByStatus(String status);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = 'UNPAID'")
    Long countPendingInvoices();
}

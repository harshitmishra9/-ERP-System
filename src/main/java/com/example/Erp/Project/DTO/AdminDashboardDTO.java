package com.example.Erp.Project.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDTO {

    private BigDecimal totalSales;
    private BigDecimal totalPurchases;
    private BigDecimal profit;
    private Long pendingInvoices;
    private Long lowStockItems;
    private Long totalUsers; 
}

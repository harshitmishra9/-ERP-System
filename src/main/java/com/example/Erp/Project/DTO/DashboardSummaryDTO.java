package com.example.Erp.Project.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class DashboardSummaryDTO {

    private BigDecimal totalSales;
    private BigDecimal totalPurchases;
    private Long pendingInvoices;
}


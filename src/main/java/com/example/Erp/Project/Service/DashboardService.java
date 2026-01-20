package com.example.Erp.Project.Service;

import java.util.List;


import com.example.Erp.Project.DTO.AdminDashboardDTO;
import com.example.Erp.Project.DTO.DashboardSummaryDTO;
import com.example.Erp.Project.Entity.*;

public interface DashboardService {

    DashboardSummaryDTO getSummary();

    List<Product> getLowStockProducts();
    AdminDashboardDTO getAdminSummary();
    



    List<Invoice> getPendingInvoices();
}

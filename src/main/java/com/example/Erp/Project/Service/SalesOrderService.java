package com.example.Erp.Project.Service;



import org.springframework.data.domain.Page;

import com.example.Erp.Project.Entity.SalesOrder;

public interface SalesOrderService {
    SalesOrder createOrder(SalesOrder order);
    SalesOrder approveOrder(Long id);
    Page<SalesOrder> listOrders(int page, int size);
}

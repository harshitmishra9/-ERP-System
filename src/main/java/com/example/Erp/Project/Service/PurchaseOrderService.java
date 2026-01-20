package com.example.Erp.Project.Service;

import org.springframework.data.domain.Page;

import com.example.Erp.Project.Entity.PurchaseOrder;

public interface PurchaseOrderService {
    PurchaseOrder createPurchaseOrder(PurchaseOrder order);
    PurchaseOrder receivePurchaseOrder(Long id);
    PurchaseOrder approve(Long id);

    Page<PurchaseOrder> listOrders(int page, int size);

}

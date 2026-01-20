package com.example.Erp.Project.Controller;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Erp.Project.Entity.PurchaseOrder;
import com.example.Erp.Project.Service.PurchaseOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('PURCHASE_MANAGER')")
    @PostMapping
    public PurchaseOrder create(@Valid @RequestBody PurchaseOrder order) {
        return service.createPurchaseOrder(order);
    }

    // ❌ RECEIVE REMOVED — GRN handles receiving

    @PreAuthorize("hasRole('ADMIN') or hasRole('PURCHASE_MANAGER')")
    @GetMapping
    public Page<PurchaseOrder> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.listOrders(page, size);
    }
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('PURCHASE_MANAGER')")
    public PurchaseOrder approve(@PathVariable Long id) {
        return service.approve(id);
    }

}



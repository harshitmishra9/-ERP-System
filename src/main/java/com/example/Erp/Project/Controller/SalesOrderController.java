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

import com.example.Erp.Project.Entity.SalesOrder;
import com.example.Erp.Project.Service.SalesOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_EXECUTIVE')")
    @PostMapping
    public SalesOrder create(@RequestBody SalesOrder order) {
        return service.createOrder(order);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public SalesOrder approve(@PathVariable Long id) {
        return service.approveOrder(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_EXECUTIVE')")
    @GetMapping
    public Page<SalesOrder> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.listOrders(page, size);
    }
}

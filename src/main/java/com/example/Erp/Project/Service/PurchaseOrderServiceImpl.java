package com.example.Erp.Project.Service;

import java.time.LocalDate;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.example.Erp.Project.Entity.PurchaseOrder;
import com.example.Erp.Project.Entity.PurchaseOrderStatus;
import com.example.Erp.Project.Entity.Supplier;
import com.example.Erp.Project.Repository.PurchaseOrderRepository;
import com.example.Erp.Project.Repository.SupplierRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository orderRepo;
    private final SupplierRepository supplierRepo;

    @Override
    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrder order) {

        Supplier supplier = supplierRepo.findById(order.getSupplier().getId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (!supplier.isActive()) {
            throw new IllegalStateException("Inactive supplier");
        }

        if (order.getExpectedDelivery().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expected delivery cannot be past date");
        }

        order.getItems().forEach(i -> i.setPurchaseOrder(order));

        order.calculateTotal();

        order.setSupplier(supplier);

        return orderRepo.save(order);
    }

    @Override
    public PurchaseOrder receivePurchaseOrder(Long id) {
        throw new UnsupportedOperationException("Receiving handled via GRN");
    }

    @Override
    public Page<PurchaseOrder> listOrders(int page, int size) {
        Pageable pageable =
                PageRequest.of(page, size, Sort.by("orderDate").descending());
        return orderRepo.findAll(pageable);
    }
    @Transactional
    public PurchaseOrder approve(Long id) {

        PurchaseOrder po = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PO not found"));

        if (po.getStatus() != PurchaseOrderStatus.CREATED) {
            throw new RuntimeException("PO already processed");
        }

        po.setStatus(PurchaseOrderStatus.APPROVED);

        return orderRepo.save(po);
    }

}

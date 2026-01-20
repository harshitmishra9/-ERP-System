package com.example.Erp.Project.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Erp.Project.Entity.*;
import com.example.Erp.Project.Repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GRNServiceImpl implements GRNService {

    private final GRNRepository grnRepo;
    private final PurchaseOrderRepository poRepo;
    private final ProductRepository productRepo;

    @Override
    @Transactional
    public GRN createGRN(Long purchaseOrderId, List<GRNItem> grnItems) {

        PurchaseOrder po = poRepo.findById(purchaseOrderId)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));

        if (po.getStatus() == PurchaseOrderStatus.COMPLETED) {
            throw new IllegalStateException("PO already completed");
        }

        if (grnRepo.existsByPurchaseOrder(po)) {
            throw new IllegalStateException("GRN already exists for this PO");
        }

        GRN grn = new GRN();
        grn.setPurchaseOrder(po);
        grn.setReceivedDate(LocalDate.now());

        for (GRNItem item : grnItems) {

            PurchaseItem poItem = po.getItems().stream()
                    .filter(i -> i.getProduct().getId().equals(item.getProduct().getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found in PO"));

            int remaining =
                    poItem.getQuantity() - poItem.getReceivedQuantity();

            if (item.getReceivedQuantity() > remaining) {
                throw new IllegalStateException("Over receiving product");
            }

            // Update PO received qty
            poItem.setReceivedQuantity(
                    poItem.getReceivedQuantity() + item.getReceivedQuantity()
            );

            // Atomic stock update
            Product product = productRepo.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setCurrentStock(
                    product.getCurrentStock() + item.getReceivedQuantity()
            );

            productRepo.save(product);

            item.setGrn(grn);
        }

        grn.setItems(grnItems);

        boolean fullyReceived = po.getItems().stream()
                .allMatch(i -> i.getReceivedQuantity().equals(i.getQuantity()));

        po.setStatus(
                fullyReceived ?
                        PurchaseOrderStatus.COMPLETED :
                        PurchaseOrderStatus.PARTIALLY_RECEIVED
        );

        poRepo.save(po);

        return grnRepo.save(grn);
    }

    @Override
    public List<GRN> getAllGRNs() {
        return grnRepo.findAll();
    }
}

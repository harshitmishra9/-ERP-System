package com.example.Erp.Project.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Erp.Project.DTO.GRNRequest;
import com.example.Erp.Project.Entity.GRN;
import com.example.Erp.Project.Entity.GRNItem;
import com.example.Erp.Project.Entity.Product;
import com.example.Erp.Project.Service.GRNService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grns")
@RequiredArgsConstructor
public class GRNController {

    private final GRNService grnService;

    @PostMapping
    @PreAuthorize("hasRole('PURCHASE_MANAGER')")
    public GRN createGRN(@RequestBody GRNRequest request) {

        List<GRNItem> grnItems = request.items().stream().map(dto -> {

            GRNItem item = new GRNItem();

            Product product = new Product();
            product.setId(dto.productId());   // ✅ DTO method

            item.setProduct(product);
            item.setReceivedQuantity(dto.receivedQuantity());

            return item;

        }).toList();

        return grnService.createGRN(request.purchaseOrderId(), grnItems);
    }

    // List all GRNs
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','INVENTORY_MANAGER')")
    public List<GRN> getAll() {
        return grnService.getAllGRNs();
    }
}

